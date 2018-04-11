package interfaces;

import exceptions.NotEnoughtSpaceException;
import exceptions.ProductNotFoundException;
import model.Lot;
import model.Stock;

import java.sql.SQLException;

public interface StockInfo {
    int totalTakenSize() throws SQLException;

    double totalTakenWeight() throws SQLException;

    Lot findAvailableSpace(int size,double weight) throws SQLException, ProductNotFoundException, NotEnoughtSpaceException;

    void importProduct(int lot_id, String product_name, int quantity) throws SQLException, ProductNotFoundException;

    Lot getEmptyLot(int size, double weight) throws SQLException, ProductNotFoundException, NotEnoughtSpaceException;

    //query koeto vrashta red ot Stocks s ime i quantity
    Stock getLot(String name, int quantity) throws SQLException, ProductNotFoundException;

    //query vryshta obshto kolko quantity imame ot daden product
    int productQuantityInStock(String name) throws SQLException;

    //namaliya quantityto na product v lot
    void exportProduct(String product_name, int quantity) throws SQLException, ProductNotFoundException;

    //namira lot s product s opredeleni broiki
    Lot lotWithProduct(String name,int quantity) throws SQLException, ProductNotFoundException;
}
