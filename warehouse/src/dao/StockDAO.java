package dao;

import exceptions.ProductNotFoundException;
import interfaces.StockInfo;
import model.Lot;
import model.Stock;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class StockDAO extends AbstractDAO implements StockInfo {

    public static void main(String[] args) throws SQLException, ProductNotFoundException {
        StockDAO st = new StockDAO();
//        st.importProduct(2, "kiwi", 12);
//        System.out.println(st.getProdFromLot().toString());
//        System.out.println(st.totalTakenSize());
//        System.out.println(st.totalTakenWeight());
        System.out.println(st.productQuantityInStock("banana"));
    }

    @Override
    public int totalTakenSize() throws SQLException {
        int counter = 0;
        String getProdSize = "SELECT sizeProduct FROM warehouse.products " +
                "WHERE nameProduct=?;";

        Connection con = getConnection();
        PreparedStatement getSize = con.prepareStatement(getProdSize);

        for (String s : getProdFromLot()) {
            ResultSet size = setProductName(getSize, s);
            if (size.next())
            counter += size.getInt(1)*productQuantityInStock(s);
        } return counter;
    }

    @Override
    public double totalTakenWeight()throws SQLException  {
        int counter = 0;
        String getTakenWeightQuery = "SELECT weightProduct FROM warehouse.products\n" +
                "WHERE nameProduct=?;";
        Connection con = getConnection();
        PreparedStatement getWeight = con.prepareStatement(getTakenWeightQuery);

        for (String s : getProdFromLot()) {
            ResultSet size = setProductName(getWeight, s);
            if (size.next())
                counter += size.getInt(1)*productQuantityInStock(s);
        } return counter;
    }

    @Override
    public Lot findAvailableSpace(int size, double weight) {
        return null;
    }

    @Override
    public void importProduct(int lot_id,  String product_name, int quantity) throws SQLException, ProductNotFoundException {
        String importProdQuery = "INSERT INTO `warehouse`.`lots_quantity` (`product_name`, `lot_id`, `product_quantity`) " +
                "VALUES (?, ?, ?);\n";
        String updateProdQuantityQuery = "UPDATE `warehouse`.`lots_quantity` SET `product_quantity`= ? " +
                "WHERE `product_name`= ?;\n";

        try (Connection con = getConnection();
             PreparedStatement importProd = con.prepareStatement(importProdQuery);
             PreparedStatement updateProd = con.prepareStatement(updateProdQuantityQuery)
        ){
            if (findProd(product_name))

                if (!getProdFromLot().contains(product_name)) {
                    insertProdInLot(importProd, lot_id, product_name, quantity);
                    System.out.println("Set new product: success ");
                } else {

                    int newQuantity = productQuantityInStock(product_name) + quantity ;
                    updateProdInLot(updateProd, product_name, newQuantity);
                    System.out.println("Update " + product_name + " quantity");
                }
        }
    }

    @Override
    public Lot getFreeLot(int size, double weight) {
        return null;
    }

    @Override
    public Stock getLot(String name, int quantity) {
        return null;
    }

    @Override
    public int productQuantityInStock(String name) throws SQLException{
        String getQuantityQuery = "SELECT SUM(product_quantity)\n" +
                "AS sumOfWeights\n" +
                "FROM warehouse.lots_quantity\n" +
                "WHERE product_name = ?;";
        try (Connection con = getConnection();
            PreparedStatement getQuantity = con.prepareStatement(getQuantityQuery);
            ResultSet quantity = setProductName(getQuantity, name)) {
            if (quantity.next()){
                return quantity.getInt(1);
            }
        }
        return 0;
    }

    @Override
    public void exportProduct(int lot_id, int quantity) {
        String exportProduct = "UPDATE `warehouse`.`lots_quantity` SET `product_quantity`=? WHERE `id`='2';\n";
    }

    @Override
    public Lot lotWithProduct(String name, int quantity) {
        return null;
    }


    private List<String> getProdFromLot() throws SQLException {
        List<String> productsList = new ArrayList<>();
        String getProdQuery = "SELECT product_name FROM warehouse.lots_quantity;";

        try (Connection con = getConnection();
             Statement getProducts = con.createStatement();
             ResultSet prod = getProducts.executeQuery(getProdQuery)
        ){
            while (prod.next()){
                productsList.add(prod.getString(1));
            }
            return productsList;
        }
    }

    private boolean findProd(String name) throws SQLException, ProductNotFoundException {

        String selectQuery = "SELECT `products`.`nameProduct`\n" +
                "FROM `warehouse`.`products`\n" +
                "WHERE `products`.`nameProduct` = ?;";

        try (Connection con = getConnection();
            PreparedStatement find = con.prepareStatement(selectQuery);
            ResultSet product = setProductName(find, name) ){
            if (product.next()){
                System.out.println("Product found");
                return true;
            } else throw new ProductNotFoundException(name);
        }
    }

    private ResultSet setProductName(PreparedStatement ps, String name) throws SQLException {

        ps.setString(1, name);
        return ps.executeQuery();
    }

    private void insertProdInLot(PreparedStatement ps, int lot, String prodName, int quantity) throws SQLException{

        ps.setString(1, prodName);
        ps.setInt(2, lot);
        ps.setInt(3, quantity);

        ps.executeUpdate();
    }

    private void updateProdInLot(PreparedStatement ps, String prodName, int quantity) throws SQLException {

        ps.setInt(1,quantity);
        ps.setString(2,prodName);
        ps.executeUpdate();
    }

}







