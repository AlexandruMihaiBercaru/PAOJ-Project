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

    public static Connection getConnectionFromInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance.connection;
    }

    // constructor privat
    private DBConnection() {
        try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);){
            if (conn != null) {
                System.out.println("Connected to the database");
            }
            this.connection = conn;

//        }catch(ClassNotFoundException exc){
//            System.err.println("Oracle driver not available: " + exc.getMessage());
        } catch (SQLException exc) {
            System.err.println("Could not connect to database: " + exc.getMessage());
        }
    }


}
