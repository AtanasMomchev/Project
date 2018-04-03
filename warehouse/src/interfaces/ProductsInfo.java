package interfaces;

import model.Product;

public interface ProductsInfo {
    Product findByName();
    void setProduct();
}
