package controller;


import exceptions.ProductNotFoundException;
import model.Product;

import java.sql.SQLException;

public class wareHouseController {
    public boolean importProduct(String name) throws ProductNotFoundException,SQLException{
        Product product = new ProductDao().findByName(name);
        
    }
}
