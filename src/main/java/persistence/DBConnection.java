package persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String JDBC_DRIVER = "oracle.jdbc.OracleDriver";
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:orcl";
    private static final String USER = "farmers_db";
    private static final String PASSWORD = "oracle";
    // referinta catre instanta clasei DBConnection
    private static DBConnection instance;
    private Connection connection;


    public static synchronized Connection getConnectionFromInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        try {
            if (instance.connection == null || instance.connection.isClosed()) {
                instance.createConnection();
            }
        } catch (SQLException e) {
            System.err.println("Error checking connection status: " + e.getMessage());
            instance.createConnection();
        }
        return instance.connection;
    }

    private DBConnection() {
        createConnection();
    }

    private void createConnection() {
        try {
            Class.forName(JDBC_DRIVER);
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            if (this.connection != null) {
                System.out.println("Connected to the database successfully");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Oracle driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Could not connect to database: " + e.getMessage());
        }
    }

    public static void closeConnection() {
        if (instance != null && instance.connection != null) {
            try {
                instance.connection.close();
                System.out.println("Database connection closed");
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}
