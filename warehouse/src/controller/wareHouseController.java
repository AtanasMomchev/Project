package controller;


import exceptions.NotEnoughtSpaceException;
import exceptions.WarehouseExceptions;
import model.Lot;
import model.Product;

import java.sql.SQLException;
import java.util.ArrayList;

public class wareHouseController {

    public ArrayList<Integer> importProduct(String name,int quantity) throws WarehouseExceptions,SQLException {
        Product product = new ProductDao().findByName(name);
        int ProductsTotalSize = product.getSize() * quantity;
        double ProductsTotalWeight = product.getWeight() * quantity;
        int availableSize = LotsDao().totalSize - StockDao().totalTakenSize;
        double availableWeight = LotsDao().totalWeight - StockDao().totalTakenWeight;

        if(ProductsTotalSize > availableSize || ProductsTotalWeight > availableWeight)
            throw new NotEnoughtSpaceException("Import package to large!");
        ArrayList<Integer> result = new ArrayList<>();

        if(checkForFreeSpace(product, quantity).get(0) != null){
              result = checkForFreeSpace(product, quantity);
        }else {
            Lot empty = StockDao().getFreeLot;
        StockDao().importProduct(empty, product, quantity);
            result.add(empty.getId());
    }
    return result;
    }

    public void exportProduct(String name, int quantity) throws WarehouseExceptions, SQLException {
        Product p = new ProductDao().findByName(name);
        while (StockDao().getProductInLots(name).hasNext()){
        }
    }

    private ArrayList<Integer> checkForFreeSpace(Product p,int quantity)throws NotEnoughtSpaceException{
        int count = quantity;
        int size = p.getSize() * quantity;
        double weight = p.getWeight() * quantity;
        ArrayList<Integer> lots = new ArrayList<Integer>();
        while (quantity >0){
            try(Lot found = StockDao().findAvailableSpace(size,weight)){
                StockDao().importProduct(found.getId(),p.getName(),count);
                lots.add(found.getId());
                quantity = quantity - count;
                size = p.getSize()*quantity;
                weight = p.getWeight()*quantity;
                count = quantity;
            }catch (Exception e){
                count--;
                size = size - p.getSize();
                weight = weight - p.getWeight();
            }
        }
        return lots;
    }
}
