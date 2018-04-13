package dao;

import exceptions.WarehouseExceptions;
import interfaces.HistoryInfo;

import java.sql.*;
import java.time.LocalDateTime;


public class HistoryDAO extends AbstractDAO implements HistoryInfo {

    public void importOrExport(String product, int quantity, String operation) throws SQLException, WarehouseExceptions {

        String importOpQuery = "INSERT INTO `warehouse`.`history` (`product_name`, `product_quantity`, " +
                "`operation`, `date`) VALUES (?, ?, ?, ?);\n";

        LocalDateTime currentDateTime = LocalDateTime.now();
        Timestamp date = Timestamp.valueOf(currentDateTime);

        try (Connection con = getConnection();
             PreparedStatement importOp = con.prepareStatement(importOpQuery)
        ){
            importOp.setString(1,product);
            importOp.setInt(2,quantity);

            if (operation.equalsIgnoreCase("import") || operation.equalsIgnoreCase("export")) {

                importOp.setString(3, operation);
            } else throw new SQLException("Invalid operation \n");

            importOp.setTimestamp(4, date);
            int update = importOp.executeUpdate();

            if (update == 1){
                System.out.println(operation + " is success \n");
            } else {
                System.err.println(operation + "fail \n");
                throw new WarehouseExceptions();
            }
        }
    }
    /*The block below is for testing the methods
    public static void main(String[] args) throws SQLException {
        HistoryDAO historyDAO = new HistoryDAO();
        historyDAO.importOrExport("sweet potato", 30, "import");

    }
    /*/
}
