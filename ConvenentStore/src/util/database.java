package util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class database {
    public static Connection connectDb(){
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection connect = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=ConvenentStore;encrypt=true;trustServerCertificate=true;", "sa", "sapassword");
            return connect;
        } catch (ClassNotFoundException | SQLException e) {
        }
        return null;
    }
}
