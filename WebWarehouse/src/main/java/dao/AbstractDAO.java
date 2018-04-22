package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AbstractDAO {

    Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();

            final String DBMS_CONN_STRING = "jdbc:mysql://localhost:3306/warehouse?verifyServerCertificate=false&useSSL=true";
            final String DBMS_USERNAME = "root";
//            final String DBMS_PASSWORD = "Nasko235";
            final String DBMS_PASSWORD = "bda6caa4";
            Connection con = DriverManager.getConnection(DBMS_CONN_STRING, DBMS_USERNAME,
                    DBMS_PASSWORD);
//            System.out.println("Connected to Warehouse");
            return con;

        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException ex){
            ex.printStackTrace();
        }
        return null;
    }
}
