package dao;

import exceptions.NotEnoughtSpaceException;
import exceptions.ProductNotFoundException;
import interfaces.StockInfo;
import model.Lot;
import model.Stock;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class StockDAO extends AbstractDAO implements StockInfo {


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
        }
        con.close();
        return counter;
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
        }
        con.close();
        return counter;
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

                if (!getProdFromLot(lot_id).contains(product_name)) {
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
    public void exportProduct(String product_name, int quantity) throws SQLException, ProductNotFoundException {
        String exportProductQuery = "UPDATE `warehouse`.`lots_quantity` SET `product_quantity`=? WHERE `product_name`=?;\n";

        try (Connection con = getConnection();
            PreparedStatement exportProduct = con.prepareStatement(exportProductQuery)
        ) {
            if (getProdFromLot().contains(product_name)) {
                int newQuantity = productQuantityInStock(product_name) - quantity;
                exportProdFromLot(exportProduct, product_name, newQuantity);
                System.out.println("Export " + quantity + " " + product_name + "s");
            }
        }

    }

    @Override
    public Lot findAvailableSpace(int size, double weight) throws SQLException, ProductNotFoundException, NotEnoughtSpaceException {

        int lotId = getAvLotId(size, weight);

        if (lotId == - 1) {
            throw new NotEnoughtSpaceException("There are no available space found");
        } else
            return new Lot(lotId, size, weight);
    }

    @Override
    public Lot getFreeLot(int size, double weight) throws SQLException, ProductNotFoundException, NotEnoughtSpaceException {

        int lotId = getAvLotId(size, weight);

        if (lotId == 0){
            return new Lot(lotId, size, weight);
        } else
            throw new NotEnoughtSpaceException("There are no free lots found");
    }

    @Override
    public Stock getLot(String name, int quantity) throws SQLException, ProductNotFoundException {
        Lot lot = lotWithProduct(name, quantity);

        int lotId = lot.getId();
        return new Stock(name, lotId, quantity);
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
    public Lot lotWithProduct(String name, int quantity) throws SQLException, ProductNotFoundException {

        LotsDAO lot = new LotsDAO();

        if (!getProdFromLot().contains(name)){
            throw new ProductNotFoundException(name);

        } else
            for (Integer id : getLotsIdsFromStockSet()){

                for (String prod : getProdFromLot(id)){

                    if (prod.equals(name)) {
                        if (productQuantityInLot(id, prod) >= quantity) {
                            return new Lot(id, lot.getLotSize(id), lot.getLotWeight(id));
                        }
                    }
                }
            }

        return null;
    }

    private int productQuantityInLot(int lot_id, String name) throws SQLException{
        String getQuantityQuery = "SELECT SUM(product_quantity)\n" +
                "AS sumOfWeights\n" +
                "FROM warehouse.lots_quantity\n" +
                "WHERE product_name = ? AND lot_id = ?;";

        try (Connection con = getConnection();
             PreparedStatement getQuantity = con.prepareStatement(getQuantityQuery);
             ResultSet quantity = setProductNameAndLot(getQuantity, name, lot_id)) {
            if (quantity.next()){
                return quantity.getInt(1);
            }
        }
        return 0;
    }

    private int getAvLotId(int size, double weight) throws SQLException, ProductNotFoundException {

        LotsDAO lots = new LotsDAO();
        ProductsDAO productsInLot = new ProductsDAO();
        int lotSize = 0;
        double lotWeight = 0;
        int productSize = 0;
        double productWeight = 0;
        int avSize = 0;
        double avWeight = 0;

        for (Integer id : getLotsIdsFromStockSet()) {
            lotSize = lots.getLotSize(id);
            lotWeight = lots.getLotWeight(id);

            for (String prod : getProdFromLot(id)) {
                productSize += productsInLot.getProductSize(prod);
                productWeight += productsInLot.getProductWeight(prod);
            }

            avSize = lotSize - productSize;
            avWeight = lotWeight - productWeight;

            if (size < avSize && weight < avWeight){
                return id;
            }
        }
        return -1;
    }

    private List<String> getProdFromLot(int lot_id) throws SQLException {
        List<String> productsList = new ArrayList<>();
        String getProdQuery = "SELECT product_name FROM warehouse.lots_quantity " +
                "WHERE lot_id=" + lot_id + ";";

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

    private List<String> getProdFromLot() throws SQLException {
        List<String> productsList = new ArrayList<>();
        String getProdQuery = "SELECT product_name FROM warehouse.lots_quantity";

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

    private ResultSet setProductNameAndLot(PreparedStatement ps, String name, int lot_id) throws SQLException {

        ps.setString(1, name);
        ps.setInt(2, lot_id);
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

    private void exportProdFromLot(PreparedStatement ps, String prodName, int quantity) throws SQLException {

        ps.setInt(1, quantity);
        ps.setString(2, prodName);
        ps.executeUpdate();
    }

    private Set<Integer> getLotsIdsFromStockSet() throws SQLException {
        String getLotIdQuery = "SELECT lot_id FROM warehouse.lots_quantity;";
        Set<Integer> lotIds = new HashSet<>();


        try (Connection con = getConnection();
             Statement getLotId = con.createStatement();
             ResultSet lotId = getLotId.executeQuery(getLotIdQuery)
        ){
            while (lotId.next()){
                lotIds.add(lotId.getInt(1));
            }
        }
        return lotIds;
    }

        /*The block below is for testing the methods
    public static void main(String[] args) throws SQLException, ProductNotFoundException, NotEnoughtSpaceException {
//        StockDAO st = new StockDAO();
//        st.importProduct(2, "kiwi", 12);
//        System.out.println(st.getProdFromLot().toString());
//        System.out.println(st.totalTakenSize());
//        System.out.println(st.totalTakenWeight());
//        System.out.println(st.productQuantityInStock("banana"));
//        st.importProduct(1, "sweet potato", 10);
//        st.exportProduct("sweet potato", 3);
//        System.out.println(st.getAvLotId(4,25));

//        Lot lot = st.findAvailableSpace(4,13);

//        Lot lot = st.lotWithProduct("kiwi", 20);
//        System.out.println("Lot ID: " + lot.getId());

//        System.out.println(st.getProdFromLot(1));
    }
    /*/

}







