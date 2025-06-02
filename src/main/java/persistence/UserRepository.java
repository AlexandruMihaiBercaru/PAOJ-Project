package persistence;

import models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static persistence.DBConnection.getConnectionFromInstance;

public class UserRepository implements GenericRepository<User>, SearchObjectByName<User> {

    private final Map<String, User> allUsers = new HashMap<>();

    private static final String INSERT_USER_SQL = "INSERT INTO app_user VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_ALL_USER_SQL = "SELECT * FROM app_user";
    private static final String SELECT_USER_SQL = "SELECT * FROM app_user WHERE username = ?";
    private static final String DELETE_USER_SQL = "DELETE FROM app_user WHERE user_id = ?";
    private static final String UPDATE_USER_SQL = "UPDATE app_user SET first_name = ?, last_name = ?, username = ?, password = ? WHERE user_id = ?";

    private final Connection conn;
    private static UserRepository instance;

    // folosesc conexiunea creata in DBConnection
    // constructor privat
    private UserRepository(){
        this.conn = getConnectionFromInstance();
    }

    public static UserRepository getInstance(){
        if(instance == null){
            instance = new UserRepository();
        }
        return instance;
    }

    @Override
    public User save(User user) {
        try(PreparedStatement statement = conn.prepareStatement(INSERT_USER_SQL))
        {
            statement.setString(1, user.getUserId());
            statement.setString(2, user.getFirstName());
            statement.setString(3, user.getLastName());
            statement.setString(4, user.getUsername());
            statement.setString(5, user.getEncryptedPassword());
            statement.setString(6, user.getRole().toLowerCase());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        allUsers.put(user.getUserId(), user);
        return user;
    }

    @Override
    public List<User> findAll() {
        try(PreparedStatement statement = conn.prepareStatement(SELECT_ALL_USER_SQL))
        {
            ResultSet rs = statement.executeQuery();
            mapResultSetToUser(rs);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new ArrayList<>(allUsers.values());
    }


    public Optional<User> findById(String userId) {
        String sql = "Select * FROM app_user WHERE user_id = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, userId);
            ResultSet rs = statement.executeQuery();

            rs.next();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(allUsers.get(userId));
    }

    @Override
    public Optional<User> findByName(String userName) {
        String id = "";
        try (PreparedStatement statement = conn.prepareStatement(SELECT_USER_SQL)) {
            statement.setString(1, userName);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                id = rs.getString("user_id");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(id).map(allUsers::get);
    }

    @Override
    public void update(User user) {
        try(PreparedStatement statement = conn.prepareStatement(UPDATE_USER_SQL)){
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getUsername());
            statement.setString(4, user.getEncryptedPassword());
            statement.setString(5, user.getUserId());

            statement.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        allUsers.put(user.getUserId(), user);
    }

    @Override
    public void delete(User user) {
        try(PreparedStatement statement = conn.prepareStatement(DELETE_USER_SQL)){
            statement.setString(1, user.getUserId());

            statement.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        allUsers.remove(user.getUserId());
    }



    private void mapResultSetToUser(ResultSet rs) throws SQLException {
        while(rs.next()){
            String userId = rs.getString("user_id");
            String userName = rs.getString("username");
            String firstName = rs.getString("first_name");
            String lastName = rs.getString("last_name");
            String encryptedPassword = rs.getString("password");
            String role = rs.getString("role");

            User user = new User(userId, firstName, lastName, userName, role, encryptedPassword);
            allUsers.put(userId, user);
        }
    }

    public Map<String, User> getAllUsers() {
        return allUsers;
    }
}
