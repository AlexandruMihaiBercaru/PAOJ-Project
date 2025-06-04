package persistence;

import exceptions.EntityNotFoundException;
import models.Farm;
import models.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class FarmRepository extends EntityRepository implements GenericRepository<Farm> {
    private static FarmRepository instance;

    private Map<String, Farm> farmStorage = new HashMap<>();

    private final UserRepository userRepository = UserRepository.getInstance();

    private FarmRepository() {super();}

    public static FarmRepository getInstance(){
        if(instance == null){
            instance = new FarmRepository();
        }
        return instance;
    }


    @Override
    public Farm save(Farm farm) {
        try(PreparedStatement stmt = conn.prepareStatement
                ("INSERT INTO farm VALUES(?, ?, ?, ?, ?, ?, ?)")){

            stmt.setString(1, farm.getFarmId());
            stmt.setString(2, farm.getOwner().getUserId());
            stmt.setString(3, farm.getFarmName());
            stmt.setString(4, farm.getAddress());
            stmt.setString(5, farm.getEmail());
            stmt.setString(6, farm.getPhone());
            stmt.setDouble(7, farm.getBudget());

            stmt.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        farmStorage.put(farm.getFarmId(), farm);
        return farm;
    }


    @Override
    public List<Farm> findAll() {
        try(PreparedStatement statement = conn.prepareStatement(
                "SELECT * FROM farm"
        )){
            ResultSet rs = statement.executeQuery();
            mapResultSetToFarm(rs);

        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        return new ArrayList<>(farmStorage.values());
    }


    private void mapResultSetToFarm(ResultSet rs) throws SQLException {
        while(rs.next()){
            String farmId = rs.getString("farm_id");
            String userId = rs.getString("user_id");
            String farmName = rs.getString("farm_name");
            String address = rs.getString("address");
            String email = rs.getString("email");
            String phone = rs.getString("phone_no");
            double budget = rs.getDouble("account_balance");

            User owner = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("There is no user with the id: " + userId));

            Farm farm = new Farm(farmName, owner, address, email, phone, budget);
            farm.setFarmId(farmId);

            farmStorage.put(farmId, farm);
        }
    }


    public Optional<Farm> findByNameOrId(String id) {
        String sql = "SELECT * FROM farm WHERE user_id=? OR farm_name=?";
        String farmId = null;
        try(PreparedStatement statement = conn.prepareStatement(sql)){
            statement.setString(1, id);
            statement.setString(2, id);
            ResultSet rs = statement.executeQuery();
            //System.out.println("Queried userId = '" + id + "'");
            //System.out.println(rs.next());
            while(rs.next()){
                farmId = rs.getString("farm_id");
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(farmId).map(farmStorage::get);
    }


    @Override
    public void update(Farm farm) {
        String sql = """
                    UPDATE farm
                    SET farm_name = ?, address = ?, email = ?, phone_no = ?, account_balance = ?
                    WHERE farm_id = ?
                    """;
        try(PreparedStatement statement = conn.prepareStatement(sql)){
            statement.setString(1, farm.getFarmName());
            statement.setString(2, farm.getAddress());
            statement.setString(3, farm.getEmail());
            statement.setString(4, farm.getPhone());
            statement.setDouble(5, farm.getBudget());
            statement.setString(6, farm.getFarmId());

            statement.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        farmStorage.put(farm.getFarmId(), farm);
    }

    @Override
    public void delete(Farm farm) {
        String sql = "DELETE FROM farm WHERE farm_id = ?";
        try(PreparedStatement statement = conn.prepareStatement(sql)){
            statement.setString(1, farm.getFarmId());

            statement.executeUpdate();

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        farmStorage.remove(farm.getFarmId());
    }
}
