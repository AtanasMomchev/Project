package controller;


import dao.LotsDAO;
import dao.ProductsDAO;
import dao.StockDAO;
import exceptions.NotEnoughtSpaceException;
import exceptions.WarehouseExceptions;
import model.Lot;
import model.Product;

import java.sql.SQLException;
import java.util.ArrayList;

public class wareHouseController {

    public ArrayList<Integer> importProduct(String name,int quantity) throws WarehouseExceptions,SQLException {
        Product product = new ProductsDAO().findByName(name);
        LotsDAO ld = new LotsDAO();
        StockDAO sd = new StockDAO();
        int ProductsTotalSize = product.getSize() * quantity;
        double ProductsTotalWeight = product.getWeight() * quantity;
        int availableSize = ld.totalSize() - sd.totalTakenSize();
        double availableWeight = ld.totalWeight() - sd.totalTakenSize();

        if(ProductsTotalSize > availableSize || ProductsTotalWeight > availableWeight)
            throw new NotEnoughtSpaceException("Import package to large!");

        ArrayList<Integer> result = new ArrayList<>();
        return result = checkForFreeSpace(product, quantity);
    }

    public void exportProduct(String name, int quantity) throws WarehouseExceptions, SQLException {
        Product p = new ProductsDAO().findByName(name);
        while (StockDao().getProductInLots(name).hasNext()){
        }
    }

    private ArrayList<Integer> checkForFreeSpace(Product p,int quantity)throws NotEnoughtSpaceException{
        StockDAO sd = new StockDAO();
        int count = quantity;
        int size = p.getSize() * quantity;
        double weight = p.getWeight() * quantity;
        ArrayList<Integer> lots = new ArrayList<Integer>();
        while (quantity >0){
            try(Lot found = sd.findAvailableSpace(size,weight)){
                sd.importProduct(found.getId(),p.getName(),count);
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
            if(count ==0) break;
        }
        if(quantity !=0) {
            while (quantity > 0) {
                size = p.getSize() * quantity;
                weight = p.getWeight() * quantity;
                try (Lot found = sd.getFreeLot(size, weight)) {
                    sd.importProduct(found.getId(), p.getName(), quantity);
                    lots.add(found.getId());
                    quantity = quantity - count;
                    size = p.getSize()*quantity;
                    weight = p.getWeight()*quantity;
                    count = quantity;
                } catch (Exception e) {
                    count--;
                    size = size - p.getSize();
                    weight = weight = p.getWeight();
                }
            }
        }
        return lots;
    }
}
