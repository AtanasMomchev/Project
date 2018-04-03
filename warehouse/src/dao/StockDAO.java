package dao;

import interfaces.StockInfo;
import model.Lot;

public class StockDAO extends AbstractDAO implements StockInfo {
    @Override
    public int totalTakenSize() {
        return 0;
    }

    @Override
    public double totalTakenWeight() {
        return 0;
    }

    @Override
    public Lot findAvailableSpace(int size, double weight) {
        return null;
    }

    @Override
    public void importProduct(int lot_id, String product_name, int quantity) {

    }

    @Override
    public Lot getFreeLot() {
        return null;
    }

    @Override
    public Lot getLotsWithProduct(String name) {
        return null;
    }
}
