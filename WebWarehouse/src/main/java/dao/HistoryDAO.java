package dao;

import exceptions.ProductNotFoundException;
import exceptions.WarehouseExceptions;
import interfaces.HistoryInfo;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public List<Timestamp> getDate() throws SQLException {
        String getDateQuery = "SELECT `history`.`date`\n" +
                "FROM `warehouse`.`history`;";
        List<Timestamp> dates = new ArrayList<>();

        try (Connection con = getConnection();
             Statement getDate = con.createStatement();
             ResultSet date = getDate.executeQuery(getDateQuery)){
            while (date.next()) {
                dates.add(date.getTimestamp(1));
            }
        }
        return dates;
    }

    public double[] getPriceAndWeight(LocalDate date) throws SQLException, ProductNotFoundException {
        String getProductAndQuantityQuery = "SELECT `history`.`product_name`,\n" +
                "`history`.`product_quantity`\n" +
                "FROM `warehouse`.`history`\n" +
                "WHERE date > " + date + ";";
        ProductsDAO pd = new ProductsDAO();
        double[] tempTurnover = new double[2];

        double cashCounter = 0;
        double weightCounter = 0;
        double price;
        double quantity;
        double weight;
        String product;

        try (Connection con = getConnection();
             Statement getProductAndQuantity = con.createStatement();
             ResultSet productAndQuantity = getProductAndQuantity.executeQuery(getProductAndQuantityQuery)){

            while (productAndQuantity.next()){
                product = productAndQuantity.getString(1);
                quantity = productAndQuantity.getInt(2);

                price = pd.getProductPrice(product);
                weight = pd.getProductWeight(product);
                cashCounter += price * quantity;
                weightCounter += weight * quantity;
            }
        }
        tempTurnover[0] = cashCounter;
        tempTurnover[1] = weightCounter;
        return tempTurnover;
    }


    private ResultSet setProdQuantiy(PreparedStatement ps, String prodName) throws SQLException {
        ps.setString(1, prodName);

        return ps.executeQuery();
    }



    /*The block below is for testing the methods
    public static void main(String[] args) throws SQLException {
        HistoryDAO historyDAO = new HistoryDAO();
        historyDAO.importOrExport("sweet potato", 30, "import");

    }
    /*/
}

