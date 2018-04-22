package dao;

import exceptions.ProductAvailableInStock;
import exceptions.ProductExistException;
import exceptions.ProductNotFoundException;
import interfaces.ProductsInfo;
import model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductsDAO extends AbstractDAO implements ProductsInfo {


    @Override
    public void setProduct(String name, int size, double weight,
                           double price) throws SQLException, ProductExistException {

        String setProdQuery = "INSERT INTO `warehouse`.`products` " +
                "(`nameProduct`, `sizeProduct`, `weightProduct`, `priceProduct`) " +
                "VALUES (?, ?, ?, ?);\n";
        try (Connection con = getConnection();
             PreparedStatement setProd = con.prepareStatement(setProdQuery)
        ) {
            insertProduct(setProd, name, size, weight, price);
            System.out.println("Set new product: success ");
        }
    }

    @Override
    public void dropProduct(String name) throws SQLException, ProductAvailableInStock {

        String deleteProdQuery = "DELETE FROM `warehouse`.`products`\n" +
                "WHERE nameProduct = ?;";

        StockDAO stockDAO = new StockDAO();
        int prodQuantity = stockDAO.productQuantityInStock(name);

        try (Connection con = getConnection();
             PreparedStatement deleteProd = con.prepareStatement(deleteProdQuery)
        ) {
            deleteProduct(deleteProd, name);
            System.out.println(name + " is dropped ");
        } catch (SQLException ex){
            throw new ProductAvailableInStock("There are " + prodQuantity + " of " + name + " in stock");
        }
    }

    private void deleteProduct(PreparedStatement ps, String name) throws SQLException {

        ps.setString(1,name);
        ps.executeUpdate();
    }

    public Product findByName(String name) throws SQLException, ProductNotFoundException {

        String selectQuery = "SELECT `products`.`nameProduct`,\n" +
                "    `products`.`sizeProduct`,\n" +
                "    `products`.`weightProduct`,\n" +
                "    `products`.`priceProduct`\n" +
                "FROM `warehouse`.`products`\n" +
                "WHERE  `products`.`nameProduct`= ?;";
        try(Connection con = getConnection();
            PreparedStatement findByName = con.prepareStatement(selectQuery);
            ResultSet product = setProductName(findByName, name)) {
            if (product.next()) {
                return new Product(product.getString("nameProduct"),
                        product.getInt("sizeProduct"),
                        product.getDouble("weightProduct"),
                        product.getDouble("priceProduct"));
            } else {
                throw new ProductNotFoundException(name);
            }
        }
    }

    private ResultSet setProductName(PreparedStatement ps, String name) throws SQLException {

        ps.setString(1,name);
        return ps.executeQuery();
    }

    private void insertProduct(PreparedStatement ps, String name, int size, double weight,
                               double price) throws SQLException, ProductExistException {

        try {
            ps.setString(1, name);
            ps.setInt(2, size);
            ps.setDouble(3, weight);
            ps.setDouble(4, price);

            ps.executeUpdate();

        } catch (SQLException icve) {
            throw new ProductExistException("This product already exist ");
        }
    }

    public int getProductSize(String prodName) throws SQLException, ProductNotFoundException {

        Product prod = findByName(prodName);
        return prod.getSize();
    }

    public double getProductWeight(String prodName)throws SQLException, ProductNotFoundException  {

        Product prod = findByName(prodName);
        return prod.getWeight();
    }

    public double getProductPrice(String prodName) throws SQLException, ProductNotFoundException {

        Product prod = findByName(prodName);
        return prod.getPrice();
    }


        /*The block below is for testing the methods
    public static void main(String[] args) throws ProductNotFoundException, SQLException, ProductExistException, ProductAvailableInStock {
        ProductsDAO pr = new ProductsDAO();
        pr.dropProduct("kiwi");
//        System.out.println(pr.findByName("banana").getName());
//        pr.setProduct("eggs", 3, 1, 2);
//        pr.dropProduct("watermelon");
    }
    /*/
}

