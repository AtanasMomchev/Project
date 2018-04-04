package interfaces;

import model.Lot;
import model.Stock;

public interface StockInfo {
    int totalTakenSize();
    double totalTakenWeight();
    Lot findAvailableSpace(int size,double weight);
    void importProduct(int lot_id, String product_name, int quantity);
    Lot getFreeLot(int size, double weight);
    //query koeto vrashta red ot Stocks s ime i quantity
    Stock getLot(String name, int quantity);
    //query vryshta obshto kolko quantity imame ot daden product
    int productQuantityInStock(String name);
    //namaliya quantityto na product v lot
    void exportProduct(int lot_id, int quantity);
    //namira lot s product s opredeleni broiki
    Lot lotWithProduct(String name,int quantity);
}
