package model;

public class Stock {
    private String Product_name;
    private String Lot_id;
    private int quantity;

    public Stock(String product_name, String lot_id, int quantity) {
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

    public String getLot_id() {
        return Lot_id;
    }

    public void setLot_id(String lot_id) {
        Lot_id = lot_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
