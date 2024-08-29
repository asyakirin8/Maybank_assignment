import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:sqlite:customers.db";  // Database URL for SQLite
    private static final String USER = "";  // User for the database (if applicable)
    private static final String PASSWORD = "";  // Password for the database (if applicable)

    // Method to establish a connection to the database
    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    // Method to initialize the database (create tables if they don't exist)
    public static void initialize() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS Customers (" +
                "CustomerID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ShortName TEXT NOT NULL, " +
                "FullName TEXT NOT NULL, " +
                "Address1 TEXT, " +
                "Address2 TEXT, " +
                "Address3 TEXT, " +
                "City TEXT, " +
                "PostalCode TEXT);";

        try (Connection conn = connect();
             var stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
