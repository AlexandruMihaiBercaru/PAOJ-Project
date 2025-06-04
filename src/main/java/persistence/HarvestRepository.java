package persistence;

import models.Harvest;
import models.HarvestOnSale;
import models.OwnedSeeds;
import models.LandLot;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class HarvestRepository extends EntityRepository implements GenericRepository<Harvest> {

    private final Map<String, Harvest> harvestStorage = new HashMap<>();

    private static HarvestRepository instance;

    private static FarmSeedsRepository seedsRepository = FarmSeedsRepository.getInstance();
    private static LandLotRepository landLotRepository = LandLotRepository.getInstance();

    private HarvestRepository() {
        super();
    }

    public static HarvestRepository getInstance() {
        if (instance == null) {
            instance = new HarvestRepository();
        }
        return instance;
    }

    @Override
    public Harvest save(Harvest harvest) {
        String harvestSql = "INSERT INTO harvest VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(harvestSql)) {
            stmt.setString(1, harvest.getHarvestId());
            stmt.setString(2, harvest.getFarmId());
            stmt.setString(3, harvest.getSeedsUsed() != null ? harvest.getSeedsUsed().getSeedsId() : null);
            stmt.setString(4, harvest.getHarvestedLand() != null ? harvest.getHarvestedLand().getLandId() : null);
            stmt.setDate(5, (Date) harvest.getHarvestDate());
            stmt.setInt(6, harvest.getYieldedQuantity());
            stmt.setString(7, harvest.isOnSale() ? "yes" : "no");

            stmt.executeUpdate();

            if (harvest instanceof HarvestOnSale) {
                saveHarvestOnSale((HarvestOnSale) harvest);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        harvestStorage.put(harvest.getHarvestId(), harvest);
        return harvest;
    }

    private void saveHarvestOnSale(HarvestOnSale harvestOnSale) throws SQLException {
        String sql = "INSERT INTO harvest_on_sale VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, harvestOnSale.getHarvestId());
            stmt.setInt(2, harvestOnSale.getQuantityOnSale());
            stmt.setInt(3, harvestOnSale.getQuantitySold());
            stmt.setDouble(4, harvestOnSale.getSalePrice());

            stmt.executeUpdate();
        }
    }

    @Override
    public List<Harvest> findAll() {
        try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM harvest")) {
            ResultSet rs = statement.executeQuery();
            mapResultSetToHarvests(rs);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new ArrayList<>(harvestStorage.values());
    }

    public ArrayList<Harvest> findAllByFarmId(String farmId) {
        ArrayList<Harvest> farmHarvests;
        try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM harvest WHERE farm_id = ?")) {
            statement.setString(1, farmId);
            ResultSet rs = statement.executeQuery();
            farmHarvests = mapResultSetToHarvests(rs);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return farmHarvests;
    }

    private ArrayList<Harvest> mapResultSetToHarvests(ResultSet rs) throws SQLException {
        ArrayList<Harvest> result = new ArrayList<>();

        while (rs.next()) {
            String harvestId = rs.getString("harvest_id");
            String farmId = rs.getString("farm_id");
            String seedsId = rs.getString("seeds_id");
            String landLotId = rs.getString("land_lot_id");
            Date harvestDate = rs.getDate("harvest_date");
            int yieldedQuantity = rs.getInt("yielded_quantity");
            String onSale = rs.getString("on_sale");

            OwnedSeeds seedsUsed = null;
            if (seedsId != null) {
                Optional<OwnedSeeds> seedsOpt =  seedsRepository.findAllByFarmId(farmId).stream()
                        .filter(ownedseeds -> ownedseeds.getSeedsId().equals(seedsId)).findFirst();
                seedsUsed = seedsOpt.orElse(null);
            }

            LandLot harvestedLand = null;
            if (landLotId != null) {
                Optional<LandLot> landLotOpt = landLotRepository.findById(landLotId);
                harvestedLand = landLotOpt.orElse(null);
            }

            Harvest harvest;
            if ("yes".equals(onSale)) {
                harvest = createHarvestOnSale(harvestId, harvestedLand, seedsUsed, harvestDate, yieldedQuantity, farmId);
            } else {
                harvest = new Harvest(harvestedLand, seedsUsed, harvestDate, yieldedQuantity, farmId);
                harvest.setHarvestId(harvestId);
            }

            result.add(harvest);
            harvestStorage.put(harvestId, harvest);
        }
        return result;
    }

    private HarvestOnSale createHarvestOnSale(String harvestId, LandLot harvestedLand, OwnedSeeds seedsUsed,
                                              Date harvestDate, int yieldedQuantity, String farmId) throws SQLException {
        String sql = "SELECT * FROM harvest_on_sale WHERE harvest_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, harvestId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int quantityOnSale = rs.getInt("quantity_on_sale");
                int quantitySold = rs.getInt("quantity_sold");
                double salePrice = rs.getDouble("sale_price");

                HarvestOnSale harvestOnSale = new HarvestOnSale(harvestedLand, seedsUsed, harvestDate, yieldedQuantity, farmId);
                harvestOnSale.setHarvestId(harvestId);
                harvestOnSale.setQuantityOnSale(quantityOnSale);
                harvestOnSale.setQuantitySold(quantitySold);
                harvestOnSale.setSalePrice(salePrice);

                return harvestOnSale;
            }
        }
        return null;
    }

    @Override
    public void update(Harvest harvest) {
        String harvestSql =
                "UPDATE harvest " +
                "SET farm_id = ?, seeds_id = ?, land_lot_id = ?, harvest_date = ?, yielded_quantity = ?, on_sale = ? " +
                "WHERE harvest_id = ?";
        try (PreparedStatement statement = conn.prepareStatement(harvestSql)) {
            statement.setString(1, harvest.getFarmId());
            statement.setString(2, harvest.getSeedsUsed() != null ? harvest.getSeedsUsed().getSeedsId() : null);
            statement.setString(3, harvest.getHarvestedLand() != null ? harvest.getHarvestedLand().getLandId() : null);
            statement.setDate(4, (Date) harvest.getHarvestDate());
            statement.setInt(5, harvest.getYieldedQuantity());
            statement.setString(6, harvest.isOnSale() ? "yes" : "no");
            statement.setString(7, harvest.getHarvestId());
            statement.executeUpdate();

            if (harvest instanceof HarvestOnSale) {
                updateHarvestOnSale((HarvestOnSale) harvest);
            } else {
                deleteHarvestOnSaleRecord(harvest.getHarvestId());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        harvestStorage.put(harvest.getHarvestId(), harvest);
    }

    private void updateHarvestOnSale(HarvestOnSale harvestOnSale) throws SQLException {
        // Check if record exists in harvest_on_sale table
        String checkSql = "SELECT COUNT(*) FROM harvest_on_sale WHERE harvest_id = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setString(1, harvestOnSale.getHarvestId());
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            if (count > 0) {
                // Update existing record
                String updateSql = "UPDATE harvest_on_sale SET quantity_on_sale = ?, quantity_sold = ?, sale_price = ? WHERE harvest_id = ?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setInt(1, harvestOnSale.getQuantityOnSale());
                    updateStmt.setInt(2, harvestOnSale.getQuantitySold());
                    updateStmt.setDouble(3, harvestOnSale.getSalePrice());
                    updateStmt.setString(4, harvestOnSale.getHarvestId());
                    updateStmt.executeUpdate();
                }
            } else {
                // Insert new record
                saveHarvestOnSale(harvestOnSale);
            }
        }
    }

    private void deleteHarvestOnSaleRecord(String harvestId) throws SQLException {
        String sql = "DELETE FROM harvest_on_sale WHERE harvest_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, harvestId);
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(Harvest harvest) {
        try {
            // Delete from harvest_on_sale table first (if exists)
            if (harvest instanceof HarvestOnSale) {
                deleteHarvestOnSaleRecord(harvest.getHarvestId());
            }

            // Delete from main harvest table
            String sql = "DELETE FROM harvest WHERE harvest_id = ?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, harvest.getHarvestId());
                statement.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        harvestStorage.remove(harvest.getHarvestId());
    }

    public Optional<Harvest> findById(String harvestId) {
        if (harvestStorage.containsKey(harvestId)) {
            return Optional.of(harvestStorage.get(harvestId));
        }

        try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM harvest WHERE harvest_id = ?")) {
            statement.setString(1, harvestId);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                ArrayList<Harvest> result = mapResultSetToHarvests(rs);
                if (!result.isEmpty()) {
                    return Optional.of(result.getFirst());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    public ArrayList<Harvest> findAllOnSale() {
        ArrayList<Harvest> onSaleHarvests = new ArrayList<>();
        try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM harvest WHERE on_sale = 'yes'")) {
            ResultSet rs = statement.executeQuery();
            onSaleHarvests = mapResultSetToHarvests(rs);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return onSaleHarvests;
    }

    public ArrayList<Harvest> findAllByLandLotId(String landLotId) {
        ArrayList<Harvest> landLotHarvests = new ArrayList<>();
        try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM harvest WHERE land_lot_id = ?")) {
            statement.setString(1, landLotId);
            ResultSet rs = statement.executeQuery();
            landLotHarvests = mapResultSetToHarvests(rs);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return landLotHarvests;
    }
}