package dao;

import exceptions.ProductExistException;
import exceptions.ProductNotFoundException;
import interfaces.ProductsInfo;
import model.Product;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductsDAO extends AbstractDAO implements ProductsInfo {

    public static void main(String[] args) throws ProductNotFoundException, SQLException, ProductExistException {
        ProductsDAO pr = new ProductsDAO();
//        System.out.println(pr.findByName("banana").getName());
        pr.setProduct("eggs", 3, 1, 2);
    }

    @Override
    public void setProduct(String name, int size, double weight,
                           double price) throws SQLException, ProductExistException {

        String setProdQuery = "INSERT INTO `warehouse`.`products` " +
                "(`nameProduct`, `sizeProduct`, `weightProduct`, `priceProduct`) " +
                "VALUES (?, ?, ?, ?);\n";
        try (
                Connection con = getConnection();
                PreparedStatement setProd = con.prepareStatement(setProdQuery)
        ) {
            insertProduct(setProd, name, size, weight, price);
            System.out.println("Set new product: success ");
        }
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
    @Override
    public void dropProduct(){

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

        } catch (MySQLIntegrityConstraintViolationException icve) {
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
}
