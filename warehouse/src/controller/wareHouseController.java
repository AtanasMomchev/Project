package controller;


import exceptions.NotEnoughtSpaceException;
import exceptions.WarehouseExceptions;
import model.Lot;
import model.Product;

import java.sql.SQLException;
import java.util.ArrayList;

public class wareHouseController {

    public void importProduct(String name,int quantity) throws WarehouseExceptions,SQLException{
        Product product = new ProductDao().findByName(name);
        int ProductsTotalSize = product.getSize() * quantity;
        double ProductsTotalWeight = product.getWeight() * quantity;
        int availableSize = LotsDao().totalSize - StockDao().totalTakenSize;
        double availableWeight = LotsDao().totalWeight - StockDao().totalTakenWeight;

        if(ProductsTotalSize < availableSize || ProductsTotalWeight < availableWeight)
            throw new NotEnoughtSpaceException("Import package to large!");

        if(checkForFreeSpace(product, quantity) == null){

        }



    }
    private ArrayList<Lot> checkForFreeSpace(Product p,int quantity)throws NotEnoughtSpaceException{
        int size = p.getSize() * quantity;
        double weight = p.getWeight() * quantity;
        ArrayList<Lot> lots = new ArrayList<Lot>();
        while (size >0){
            try(Lot found = StockDao().findAvailableSpace(size,weight)){
                lots.add(found);
                size = size - p.getSize();
                weight = weight - p.getWeight();
                quantity--;
            }catch (Exception e){

            }
        }
        return lots;
    }
}
