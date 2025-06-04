package persistence;

import models.OwnedSeeds;
import models.SeedsLot;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class FarmSeedsRepository extends EntityRepository implements GenericRepository<OwnedSeeds>{


    private final Map<String, OwnedSeeds> seedsStorage = new HashMap<>();

    private static FarmSeedsRepository instance;

    private static SeedsLotRepository lotRepository = SeedsLotRepository.getInstance();

    private FarmSeedsRepository(){super();}

    public static FarmSeedsRepository getInstance(){
        if(instance == null){
            instance = new FarmSeedsRepository();
        }
        return instance;
    }

    @Override
    public OwnedSeeds save(OwnedSeeds ownedSeeds) {
        String sql = "INSERT INTO farm_seeds VALUES (?, ?, ?, ?, ?, ?)";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setString(1, ownedSeeds.getSeedsId());
            stmt.setString(2, ownedSeeds.getLotOfOrigin().getLotId());
            stmt.setDate(3, Date.valueOf(ownedSeeds.getBuyingDate()));
            stmt.setInt(4, ownedSeeds.getQuantityBought());
            stmt.setInt(5, ownedSeeds.getQuantityAvailable());
            stmt.setString(6, ownedSeeds.getFarmId());

            stmt.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        seedsStorage.put(ownedSeeds.getSeedsId(), ownedSeeds);
        return ownedSeeds;
    }

    @Override
    public List<OwnedSeeds> findAll() {
        try(PreparedStatement statement = conn.prepareStatement("SELECT * FROM farm_seeds"))
        {
            ResultSet rs = statement.executeQuery();
            mapResultSetToFarmSeeds(rs);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new ArrayList<>(seedsStorage.values());
    }

    public ArrayList<OwnedSeeds> findAllByFarmId(String farmId) {
        ArrayList<OwnedSeeds> userSeedInventory;
        try(PreparedStatement statement = conn.prepareStatement("SELECT * FROM farm_seeds WHERE farm_id = ?"))
        {
            statement.setString(1, farmId);
            ResultSet rs = statement.executeQuery();
            userSeedInventory = mapResultSetToFarmSeeds(rs);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userSeedInventory;
    }

    private ArrayList<OwnedSeeds> mapResultSetToFarmSeeds(ResultSet rs) throws SQLException {
        ArrayList<OwnedSeeds> result = new ArrayList<>();
        while(rs.next()){
            String seedsId = rs.getString("seeds_id");
            String seeds_lot_id = rs.getString("seeds_lot_id");
            LocalDate buyingDate = rs.getDate("buying_date").toLocalDate();
            int quantityBought = rs.getInt("quantity_bought");
            int quantityAvailable = rs.getInt("quantity_available");
            String farmId = rs.getString("farm_id");

            SeedsLot lotOfOrigin = lotRepository.findById(seeds_lot_id).get();
            OwnedSeeds seeds = new OwnedSeeds(lotOfOrigin, buyingDate, quantityBought, quantityAvailable, farmId);
            seeds.setSeedsId(seedsId);

            result.add(seeds);
            seedsStorage.put(seedsId, seeds);
        }
        return result;
    }

    // updates a row when seeds are used for planting
    @Override
    public void update(OwnedSeeds ownedSeeds) {
        String sql = "UPDATE farm_seeds SET quantity_available = ? WHERE seeds_id = ?";
        try(PreparedStatement statement = conn.prepareStatement(sql)){
            statement.setInt(1, ownedSeeds.getQuantityAvailable());
            statement.setString(2, ownedSeeds.getSeedsId());
            statement.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        seedsStorage.put(ownedSeeds.getSeedsId(), ownedSeeds);
    }

    // only deletes a row when the available quantity is 0
    @Override
    public void delete(OwnedSeeds ownedSeeds) {
        String sql = "DELETE FROM farm_seeds WHERE seeds_id = ?";
        try(PreparedStatement statement = conn.prepareStatement(sql)){
            statement.setString(1, ownedSeeds.getSeedsId());

            statement.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        seedsStorage.remove(ownedSeeds.getSeedsId());
    }


}
