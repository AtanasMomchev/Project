package controller;


import dao.HistoryDAO;
import dao.LotsDAO;
import dao.ProductsDAO;
import dao.StockDAO;
import exceptions.NotEnoughtProductsInSupply;
import exceptions.NotEnoughtSpaceException;
import exceptions.ProductNotFoundException;
import exceptions.WarehouseExceptions;
import model.Lot;
import model.Product;
import model.Stock;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class wareHouseController {
    private StockDAO sd = new StockDAO();
    private LotsDAO ld = new LotsDAO();
    private ProductsDAO pd = new ProductsDAO();
    private HistoryDAO hd = new HistoryDAO();

    public ArrayList<Stock> importProduct(String name,int quantity) throws WarehouseExceptions,SQLException {
        Product product = pd.findByName(name);
        int ProductsTotalSize = product.getSize() * quantity;
        double ProductsTotalWeight = product.getWeight() * quantity;
        int availableSize = ld.totalSize() - sd.totalTakenSize();
        double availableWeight = ld.totalWeight() - sd.totalTakenWeight();
        //check if there is enough space in the warehouse for the import
        if(ProductsTotalSize > availableSize || ProductsTotalWeight > availableWeight)
            throw new NotEnoughtSpaceException("Import package to large!");
        //result is filled with lots_id in which the product is placed
        ArrayList<Stock> result = new ArrayList<>();
        hd.importOrExport(name,quantity,"import");
        return result = findLotsToImport(product, quantity);
    }

    public ArrayList<Stock> exportProduct(String name, int quantity) throws WarehouseExceptions, SQLException {
        Product p = new ProductsDAO().findByName(name);
        int q = quantity;
        //check if there is enough products in the warehouse to export
        if(sd.productQuantityInStock(name) < quantity) throw new NotEnoughtProductsInSupply("Insufficient quantity in supply!");

        ArrayList<Stock> result = new ArrayList<>();
        int count = quantity;
        int index = 0;
        while (quantity>0){
            try{
                Lot found = sd.lotWithProduct(name,count);
//                result.add(sd.getLot(found.getId(),name,quantity));
                result.add(new Stock(name, found.getId(),quantity));
                sd.exportProduct(found.getId(),name,count);
                quantity = quantity - count;
                count = quantity;
                index++;
            }catch (Exception e){

                count--;
            }
        }
        hd.importOrExport(name,q,"export");
        return result;
    }

    private ArrayList<Stock> findLotsToImport(Product p,int quantity)throws NotEnoughtSpaceException{
        int count = quantity;
        int size = p.getSize() * quantity;
        double weight = p.getWeight() * quantity;
        ArrayList<Stock> result = new ArrayList<>();
        //finding available space in slots that have products
        while (quantity >0){
            try{
                Lot found = sd.findAvailableSpace(size,weight);
                sd.importProduct(found.getId(),p.getName(),count);
                result.add(new Stock(p.getName(),found.getId(),quantity));
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
        count = quantity;
        if(quantity !=0) {
            //if there are any products left put them in a empty slot/s
            size = p.getSize() * quantity;
            weight = p.getWeight() * quantity;
            while (quantity > 0) {
                try {
                    Lot found = sd.getEmptyLot(size, weight);
                    sd.importProduct(found.getId(), p.getName(), quantity);
                    result.add(new Stock(p.getName(),found.getId(),quantity));
                    quantity = quantity - count;
                    size = p.getSize()*quantity;
                    weight = p.getWeight()*quantity;
                    count = quantity;
                } catch (Exception e) {
                    count--;
                    size = size - p.getSize();
                    weight = weight - p.getWeight();
                }
                if(count == 0) break;
            }
        }
        return result;
    }
    public ArrayList<Stock> removeLotAndReangeProducts(int lot_id) throws SQLException,NotEnoughtSpaceException,WarehouseExceptions{
        int availableSize = ld.totalSize() - sd.totalTakenSize();
        double availableWeight = ld.totalWeight() - sd.totalTakenWeight();
        int sizeInLot = sd.getTotalTakenSizeInLot(lot_id);
        double weightInLot = sd.getTotalTakenWeightInLot(lot_id);
        if(availableSize < sizeInLot || availableWeight < weightInLot) throw new NotEnoughtSpaceException("No space to rearange products!");

        int quantity;
        ArrayList<Stock> rearrangedProducts = null;
        for(String product : sd.getProdFromLot(lot_id)){
            quantity = sd.getQuantityOfProductInOneLot(lot_id,product);
            rearrangedProducts = importProduct(product,quantity);
            sd.deleteRow(lot_id);
            if(!ld.deleteLot(lot_id)) throw new WarehouseExceptions("Could not delete lot for some reason!");
        }
        return rearrangedProducts;
    }

    public double[] totalTakenSpaceInWarehouse()throws SQLException{
        double [] totalTakenSpace = new double[2];
        totalTakenSpace [0] = sd.totalTakenSize();
        totalTakenSpace [1] = sd.totalTakenWeight();
        return totalTakenSpace;
    }

    public double[] totalFreeSpaceInWarehouse()throws  SQLException{
        double [] availableSpace = new double[2];
        availableSpace [0] = ld.totalSize() - sd.totalTakenSize();
        availableSpace [1] = ld.totalWeight() - sd.totalTakenWeight();
        return availableSpace;
    }

    public double totalPriceOfProductsInLots()throws SQLException,WarehouseExceptions{
        double moneyTotal = 0;
        int quantity;

        for(int id : sd.getLotsIdsFromStockSet()){
            for(String product : sd.getProdFromLot(id)){
                quantity = sd.getQuantityOfProductInOneLot(id,product);
                moneyTotal += pd.findByName(product).getPrice()*quantity;
            }
        }
        return moneyTotal;
    }

    public double averageTransactionPerDay(int days) throws SQLException {
        List<Timestamp> dates = hd.getDate();
        LocalDateTime targetDate = LocalDateTime.now().minusDays(days);
        double counter = 0;

        for (Timestamp ts : dates){
            if (ts.toLocalDateTime().isAfter(targetDate)){
                counter++;
            }
        }
        return counter/days;
    }

    public double[] turnoverPriceAndWeight(int days) throws SQLException, ProductNotFoundException {
        LocalDate targetDate = LocalDate.now().minusDays(days);
        double[] priceAndWeight;

        priceAndWeight = hd.getPriceAndWeight(targetDate);

        return priceAndWeight;
    }

    public void productsAndQuantity(int id_lot) throws SQLException {
        int quantity;
        System.out.format("%-15s | %-15s \n", "product", "quantity");
        System.out.println("---------------- ---------");

        for (String s :sd.getProdFromLot(id_lot)) {
            quantity = sd.getQuantityOfProductInOneLot(id_lot, s);
            System.out.format("%-15s | %-15d \n", s, quantity);
        }
    }

    public void productQuantityAndDistribution(String name) throws SQLException {

        System.out.println(sd.productQuantityInStock(name));
        System.out.println(sd.getLotsIdsFromStockSet());
        for (Integer i : sd.getLotsIdsFromStockSet())
        System.out.println(sd.getQuantityOfProductInOneLot(i, name));

    }

    public static void main(String[] args) throws SQLException,WarehouseExceptions{
        wareHouseController whc = new wareHouseController();
//        whc.importProduct("eggs", 2);
//        whc.productsAndQuantity(1);

        whc.productQuantityAndDistribution("eggs");
//        for (Double d : whc.turnoverPriceAndWeight(2))
//        System.out.println(d);
//        System.out.println(whc.averageTransactionPerDay(2));
//        ArrayList<Stock> test = whc.removeLotAndReangeProducts(7);

//        for (Double d : whc.totalFreeSpaceInWarehouse()){
//            System.out.println(d);
//        }
//        System.out.println(whc.totalPriceOfProductsInLots());

//        for(int i =0;i<test.size();i++){
//            System.out.printf("%d, %s,%d",test.get(i).getLot_id(),test.get(i).getProduct_name(),test.get(i).getQuantity());
//        }
//        ArrayList<Stock> stocks = whc.exportProduct("orange", 2);

    }
}
