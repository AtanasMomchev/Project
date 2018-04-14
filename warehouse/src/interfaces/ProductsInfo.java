package interfaces;

import exceptions.ProductAvailableInStock;
import exceptions.ProductExistException;
import exceptions.ProductNotFoundException;
import model.Product;

import java.sql.SQLException;

public interface ProductsInfo {
    Product findByName(String name) throws SQLException, ProductNotFoundException;
    void setProduct(String name, int size, double weight,
                    double price)throws SQLException, ProductExistException;
    public void dropProduct(String name) throws SQLException, ProductAvailableInStock;
}
