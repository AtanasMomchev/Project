package model;

public class Stock {
    private String Product_name;
    private int Lot_id;
    private int quantity;

    public Stock(String product_name, int lot_id, int quantity) {
        Product_name = product_name;
        Lot_id = lot_id;
        this.quantity = quantity;
    }

    public String getProduct_name() {
        return Product_name;
    }

    public void setProduct_name(String product_name) {
        Product_name = product_name;
    }

    public int getLot_id() {
        return Lot_id;
    }

    public void setLot_id(int lot_id) {
        Lot_id = lot_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
