package controller;


import dao.LotsDAO;
import dao.ProductsDAO;
import dao.StockDAO;
import exceptions.NotEnoughtProductsInSupply;
import exceptions.NotEnoughtSpaceException;
import exceptions.WarehouseExceptions;
import model.Lot;
import model.Product;
import model.Stock;

import java.sql.SQLException;
import java.util.ArrayList;

public class wareHouseController {
    StockDAO sd = new StockDAO();
    LotsDAO ld = new LotsDAO();

    public ArrayList<Integer> importProduct(String name,int quantity) throws WarehouseExceptions,SQLException {
        Product product = new ProductsDAO().findByName(name);
        int ProductsTotalSize = product.getSize() * quantity;
        double ProductsTotalWeight = product.getWeight() * quantity;
        int availableSize = ld.totalSize() - sd.totalTakenSize();
        double availableWeight = ld.totalWeight() - sd.totalTakenWeight();
        //check if there is enough space in the warehouse for the import
        if(ProductsTotalSize > availableSize || ProductsTotalWeight > availableWeight)
            throw new NotEnoughtSpaceException("Import package to large!");
        //result is filled with lots_id in which the product is placed
        ArrayList<Integer> result = new ArrayList<>();

        return result = checkForFreeSpace(product, quantity);
    }

    public ArrayList<Stock> exportProduct(String name, int quantity) throws WarehouseExceptions, SQLException {
        Product p = new ProductsDAO().findByName(name);
        //check if there is enough products in the warehouse to export
        if(sd.productQuantityInStock(name) < quantity) throw new NotEnoughtProductsInSupply("Insufficient quantity in supply!");

        ArrayList<Stock> result = new ArrayList<>();
        int count = quantity;
        int index = 0;
        while (quantity>0){
            try(sd.lotWithProduct(name,count)){
                Lot found = sd.lotWithProduct(name,count);
                result.add(sd.getLot(name,quantity));
                result.get(index).setQuantity(quantity);
                sd.exportProduct(name,count);
                quantity = quantity - count;
                count = quantity;
                index++;
            }catch (Exception e){
                count--;
            }
        }

        return result;
    }

    private ArrayList<Integer> checkForFreeSpace(Product p,int quantity)throws NotEnoughtSpaceException{
        int count = quantity;
        int size = p.getSize() * quantity;
        double weight = p.getWeight() * quantity;
        ArrayList<Integer> lots = new ArrayList<Integer>();
        //finding available space in slots that have products
        while (quantity >0){
            try(sd.findAvailableSpace(size,weight)){
                Lot found = sd.findAvailableSpace(size,weight);
                sd.importProduct(found.getId(),p.getName(),count);
                lots.add(found.getId());
                quantity = quantity - count;
                size = p.getSize()*quantity;
                weight = p.getWeight()*quantity;
                count = quantity;
            }catch (Exception e){
                if(e.equals("NotEnoughtSpaceException")) {
                    count--;
                    size = size - p.getSize();
                    weight = weight - p.getWeight();
                }
            }
            if(count ==0) break;
        }
        if(quantity !=0) {
            //if there are any products left put them in a empty slot/s
            size = p.getSize() * quantity;
            weight = p.getWeight() * quantity;
            while (quantity > 0) {
                try (sd.getFreeLot(size, weight)) {
                    Lot found = sd.getFreeLot(size, weight);
                    sd.importProduct(found.getId(), p.getName(), quantity);
                    lots.add(found.getId());
                    quantity = quantity - count;
                    size = p.getSize()*quantity;
                    weight = p.getWeight()*quantity;
                    count = quantity;
                } catch (Exception e) {
                    count--;
                    size = size - p.getSize();
                    weight = weight - p.getWeight();
                }
            }
        }
        return lots;
    }
    public void removeLotAndReangeProducts(int lot_id){
        sd.
    }
}
