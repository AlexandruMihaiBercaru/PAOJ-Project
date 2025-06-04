package persistence;

import models.LandLot;
import models.ArableLand;
import models.Orchard;
import models.OwnedSeeds;
import models.Crop;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class LandLotRepository extends EntityRepository implements GenericRepository<LandLot> {

    private final Map<String, LandLot> landLotStorage = new HashMap<>();

    private static LandLotRepository instance;

    private static FarmSeedsRepository seedsRepository = FarmSeedsRepository.getInstance();
    private static CropRepository cropRepository = CropRepository.getInstance();

    private LandLotRepository() {
        super();
    }

    public static LandLotRepository getInstance() {
        if (instance == null) {
            instance = new LandLotRepository();
        }
        return instance;
    }

    @Override
    public LandLot save(LandLot landLot) {
        // First insert into land_lot table
        String landLotSql = "INSERT INTO land_lot VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(landLotSql)) {
            stmt.setString(1, landLot.getLandId());
            stmt.setString(2, landLot.getLandName());
            stmt.setString(3, landLot.getFarmId());
            stmt.setDouble(4, landLot.getArea());
            stmt.setString(5, landLot.getLandUsage());

            stmt.executeUpdate();

            // Insert into specific subclass table based on land usage
            if (landLot instanceof ArableLand) {
                saveArableLand((ArableLand) landLot);
            } else if (landLot instanceof Orchard) {
                saveOrchard((Orchard) landLot);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        landLotStorage.put(landLot.getLandId(), landLot);
        return landLot;
    }

    private void saveArableLand(ArableLand arableLand) throws SQLException {
        String sql = "INSERT INTO arable_land VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, arableLand.getLandId());
            stmt.setString(2, arableLand.getSeedsUsed() != null ? arableLand.getSeedsUsed().getSeedsId() : null);
            stmt.setDate(3, arableLand.getPlantingDate() != null ? (Date) arableLand.getPlantingDate() : null);
            stmt.setString(4, arableLand.isCurrentlyCultivated() ? "yes" : "no");

            stmt.executeUpdate();
        }
    }

    private void saveOrchard(Orchard orchard) throws SQLException {
        String sql = "INSERT INTO orchard VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, orchard.getLandId());
            stmt.setString(2, orchard.getCrop().getCropId());
            stmt.setInt(3, orchard.getAge());

            stmt.executeUpdate();
        }
    }

    @Override
    public List<LandLot> findAll() {
        try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM land_lot")) {
            ResultSet rs = statement.executeQuery();
            mapResultSetToLandLots(rs);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new ArrayList<>(landLotStorage.values());
    }

    public ArrayList<LandLot> findAllByFarmId(String farmId) {
        ArrayList<LandLot> farmLandLots;
        try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM land_lot WHERE farm_id = ?")) {
            statement.setString(1, farmId);
            ResultSet rs = statement.executeQuery();
            farmLandLots = mapResultSetToLandLots(rs);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return farmLandLots;
    }

    private ArrayList<LandLot> mapResultSetToLandLots(ResultSet rs) throws SQLException {
        ArrayList<LandLot> result = new ArrayList<>();

        while (rs.next()) {
            String landLotId = rs.getString("land_lot_id");
            String lotName = rs.getString("lot_name");
            String farmId = rs.getString("farm_id");
            double area = rs.getDouble("area");
            String landUsage = rs.getString("land_usage");

            LandLot landLot = switch (landUsage) {
                case "arable" -> createArableLand(landLotId, lotName, area, landUsage, farmId);
                case "orchard" -> createOrchard(landLotId, lotName, area, landUsage, farmId);
                default -> null;
            };

            landLot.setLandId(landLotId);
            result.add(landLot);
            landLotStorage.put(landLotId, landLot);
        }
        return result;
    }

    private ArableLand createArableLand(String landLotId, String lotName, double area, String landUsage, String farmId) throws SQLException {
        String sql = "SELECT * FROM arable_land WHERE land_lot_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, landLotId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String seedsId = rs.getString("seeds_id");
                Date plantingDate = rs.getDate("planting_date");
                String isCultivated = rs.getString("is_cultivated");

                OwnedSeeds seedsUsed = null;
                if (seedsId != null) {

                    Optional<OwnedSeeds> seedsOpt = seedsRepository.findAllByFarmId(farmId).stream()
                            .filter(ownedseeds -> ownedseeds.getSeedsId().equals(seedsId)).findFirst();
                    seedsUsed = seedsOpt.orElse(null);
                }

                boolean isCurrentlyCultivated = "yes".equals(isCultivated);

                ArableLand arableLand = new ArableLand(lotName, area, landUsage, farmId, seedsUsed, plantingDate, isCurrentlyCultivated);
                arableLand.setLandId(landLotId);
                return arableLand;
            }
        }
        return null;
    }

    private Orchard createOrchard(String landLotId, String lotName, double area, String landUsage, String farmId) throws SQLException {
        String sql = "SELECT * FROM orchard WHERE land_lot_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, landLotId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String cropId = rs.getString("crop_id");
                int age = rs.getInt("age");

                Crop crop = cropRepository.findById(cropId).orElse(null);

                Orchard orchard = new Orchard(lotName, area, landUsage, farmId, crop, age);
                orchard.setLandId(landLotId);
                return orchard;
            }
        }
        return null;
    }

    @Override
    public void update(LandLot landLot) {
        // Update main land_lot table
        String landLotSql = "UPDATE land_lot SET lot_name = ?, area = ?, land_usage = ? WHERE land_lot_id = ?";
        try (PreparedStatement statement = conn.prepareStatement(landLotSql)) {
            statement.setString(1, landLot.getLandName());
            statement.setDouble(2, landLot.getArea());
            statement.setString(3, landLot.getLandUsage());
            statement.setString(4, landLot.getLandId());
            statement.executeUpdate();

            // Update specific subclass table
            if (landLot instanceof ArableLand) {
                updateArableLand((ArableLand) landLot);
            } else if (landLot instanceof Orchard) {
                updateOrchard((Orchard) landLot);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        landLotStorage.put(landLot.getLandId(), landLot);
    }

    private void updateArableLand(ArableLand arableLand) throws SQLException {
        String sql = "UPDATE arable_land SET seeds_id = ?, planting_date = ?, is_cultivated = ? WHERE land_lot_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, arableLand.getSeedsUsed() != null ? arableLand.getSeedsUsed().getSeedsId() : null);
            stmt.setDate(2, arableLand.getPlantingDate() != null ? (Date)arableLand.getPlantingDate() : null);
            stmt.setString(3, arableLand.isCurrentlyCultivated() ? "yes" : "no");
            stmt.setString(4, arableLand.getLandId());
            stmt.executeUpdate();
        }
    }

    private void updateOrchard(Orchard orchard) throws SQLException {
        String sql = "UPDATE orchard  SET  age = ? WHERE land_lot_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(2, orchard.getAge());
            stmt.setString(3, orchard.getLandId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(LandLot landLot) {
        // Delete from specific subclass tables first (due to foreign key constraints)
        try {
            if (landLot instanceof ArableLand) {
                deleteArableLand(landLot.getLandId());
            } else if (landLot instanceof Orchard) {
                deleteOrchard(landLot.getLandId());
            }

            // Delete from main land_lot table
            String sql = "DELETE FROM land_lot WHERE land_lot_id = ?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, landLot.getLandId());
                statement.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        landLotStorage.remove(landLot.getLandId());
    }

    private void deleteArableLand(String landLotId) throws SQLException {
        String sql = "DELETE FROM arable_land WHERE land_lot_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, landLotId);
            stmt.executeUpdate();
        }
    }

    private void deleteOrchard(String landLotId) throws SQLException {
        String sql = "DELETE FROM orchard WHERE land_lot_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, landLotId);
            stmt.executeUpdate();
        }
    }

    public Optional<LandLot> findById(String landLotId) {
        if (landLotStorage.containsKey(landLotId)) {
            return Optional.of(landLotStorage.get(landLotId));
        }

        try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM land_lot WHERE land_lot_id = ? OR  LOT_NAME = ?")) {
            statement.setString(1, landLotId);
            statement.setString(2, landLotId);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                ArrayList<LandLot> result = mapResultSetToLandLots(rs);
                if (!result.isEmpty()) {
                    return Optional.of(result.getFirst());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }
}