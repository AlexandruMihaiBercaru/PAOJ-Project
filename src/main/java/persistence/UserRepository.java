package persistence;

import models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static persistence.DBConnection.getConnectionFromInstance;

public class UserRepository implements GenericRepository<User>{

    private final Map<String, User> allUsers = new HashMap<>();

    private static final String INSERT_USER_SQL = "INSERT INTO app_user VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_ALL_USER_SQL = "SELECT * FROM app_user";
    private static final String SELECT_USER_SQL = "SELECT * FROM app_user WHERE user_id = ?";
    private static final String DELETE_USER_SQL = "DELETE FROM app_user WHERE user_id = ?";
    private static final String UPDATE_USER_SQL = "UPDATE app_user SET username = ?, password = ? WHERE user_id = ?";

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
    public User save(User obj) {
        try(PreparedStatement statement = conn.prepareStatement(INSERT_USER_SQL))
        {
            statement.setString(1, obj.getFirstName());
            statement.setString(2, obj.getLastName());
            statement.setString(3, obj.getUsername());
            statement.setString(4, obj.getEncryptedPassword());
            statement.setString(5, obj.getRole().toLowerCase());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        allUsers.put(obj.getUserId(), obj);
        return null;
    }

    @Override
    public List<User> findAll() {
        try(PreparedStatement statement = conn.prepareStatement(SELECT_ALL_USER_SQL))
        {
            ResultSet rs = statement.executeQuery();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new ArrayList<>(allUsers.values());
    }

    // TODO
    @Override
    public Optional<User> findById(String id) {
        return Optional.empty();
    }

    //TODO
    @Override
    public void update(User obj) {

    }

    //TODO
    @Override
    public void delete(User obj) {

    }

    private void extractUserFromResultSet(ResultSet rs) throws SQLException {
        while(rs.next()){
            String userId = rs.getString("user_id");
            String username = rs.getString("username");
            String firstName = rs.getString("first_name");
            String lastName = rs.getString("last_name");
            String encryptedPassword = rs.getString("encrypted_password");
            String role = rs.getString("role");

            User user = new User(firstName, lastName, encryptedPassword, role);
            allUsers.put(userId, user);
        }
    }
}
