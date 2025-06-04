package persistence;

import models.*;
import oracle.jdbc.proxy.annotation.Pre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static persistence.DBConnection.getConnectionFromInstance;

public class SeedsLotRepository extends EntityRepository implements GenericRepository<SeedsLot> {

    private static SeedsLotRepository instance;

    private CropRepository cropRepository = CropRepository.getInstance();

    private Map<String, SeedsLot> seedLotsStorage = new HashMap<>();

    private SeedsLotRepository() {super();}

    public static SeedsLotRepository getInstance() {
        if (instance == null) {
            instance = new SeedsLotRepository();
        }
        return instance;
    }


    @Override
    public SeedsLot save(SeedsLot seedsLot) {
        String sql = "INSERT INTO seeds_lot VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setString(1, seedsLot.getLotId());
            stmt.setString(2, seedsLot.getCrop().getCropId());
            stmt.setString(3, seedsLot.getPlantingPeriod());
            stmt.setString(4, seedsLot.getHarvestingPeriod());
            stmt.setDouble(5, seedsLot.getPricePerUnit());
            stmt.setString(6, seedsLot.getSaleUnit().getLiteral());
            stmt.setInt(7, seedsLot.getPlantsPerSqMeter());
            stmt.setInt(8, seedsLot.getExpectedYieldPerSqMeter());
            stmt.setInt(9, seedsLot.getAvailableQuantity());

            stmt.executeUpdate();



        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        seedLotsStorage.put(seedsLot.getLotId(), seedsLot);
        return seedsLot;
    }

    @Override
    public List<SeedsLot> findAll() {
        try(PreparedStatement stmt = conn.prepareStatement("""
                SELECT * FROM seeds_lot
                JOIN crop ON seeds_lot.crop_id = crop.crop_id
            """)){
            ResultSet rs = stmt.executeQuery();
            mapResultSetToSeedsLot(rs);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return new ArrayList<>(seedLotsStorage.values());
    }

    private void mapResultSetToSeedsLot(ResultSet rs) throws SQLException {
        while(rs.next()){
            String seedsLotId = rs.getString("seeds_lot_id");
            String plantingPeriod = rs.getString("planting_period");
            String harvestingPeriod = rs.getString("harvesting_period");
            double pricePerUnit = rs.getDouble("price_per_unit");
            String saleUnit = rs.getString("sale_unit");
            int plantsPerSqMeter = rs.getInt("plants_per_sq_meter");
            int expectedYieldPerSqMeter = rs.getInt("expected_yield_per_sq_meter");
            int availableQuantity = rs.getInt("available_quantity");

            Crop cropObtainedFromSeedsLot = cropRepository.getOneCropFromResultSet(rs);

            SeedsLot lot = new SeedsLot(cropObtainedFromSeedsLot, plantingPeriod, harvestingPeriod, pricePerUnit,
                    saleUnit, plantsPerSqMeter, expectedYieldPerSqMeter, availableQuantity);
            lot.setLotId(seedsLotId);
            seedLotsStorage.put(seedsLotId, lot);
        }
    }

    /// updates the quantity available
    @Override
    public void update(SeedsLot lot) {
        String sql = "UPDATE seeds_lot SET available_quantity = ? WHERE seeds_lot_id = ?";
        try(PreparedStatement statement = conn.prepareStatement(sql)){

            statement.setInt(1, lot.getAvailableQuantity());
            statement.setString(2, lot.getLotId());

            statement.executeUpdate();


        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        seedLotsStorage.put(lot.getLotId(), lot);
    }

    @Override
    public void delete(SeedsLot lot) {
        String sql = "DELETE FROM seeds_lot WHERE seeds_lot_id = ?";
        try(PreparedStatement statement = conn.prepareStatement(sql)){
            statement.setString(1, lot.getLotId());

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        seedLotsStorage.remove(lot.getLotId());
    }


    public Optional<SeedsLot> findById(String seedsLotId) {
        String sql = "Select * FROM SEEDS_LOT WHERE seeds_lot_id = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, seedsLotId);
            ResultSet rs = statement.executeQuery();
            rs.next();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(seedLotsStorage.get(seedsLotId));
    }


}
