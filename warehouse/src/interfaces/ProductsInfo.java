package interfaces;

import model.Product;

public interface ProductsInfo {
    int totalSize();
    int totalWeight();
    Product findByName();
    void setProduct();

}
