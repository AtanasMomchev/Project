package interfaces;

import model.Lot;

public interface StockInfo {
    int totalTakenSize();
    double totalTakenWeight();
    Lot findAvailableSpace(int size,double weight);
    void importProduct(int lot_id, String product_name, int quantity);
    Lot getFreeLot(int size, double weight);
    Lot getLotsWithProduct(String name);
}
