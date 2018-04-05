package dao;

import interfaces.LotsInfo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LotsDAO extends AbstractDAO implements LotsInfo {

    @Override
    public double totalWeight() {

        String getWeightQuery = "SELECT SUM(lots.weightCapacityLots)\n" +
                "AS sumOfWeights\n" +
                "FROM warehouse.lots;\n";
        try (Connection con = getConnection();
             Statement getWeight = con.createStatement();
             ResultSet weight = getWeight.executeQuery(getWeightQuery)){

            if (weight.next()) {
                return weight.getDouble("sumOfWeights");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int totalSize() {
        String getSizeQuery = "SELECT SUM(lots.sizeLots)\n" +
                "AS sumOfSizes\n" +
                "FROM warehouse.lots;\n";
        try (
                Connection con = getConnection();
                Statement getSize = con.createStatement();
                ResultSet size = getSize.executeQuery(getSizeQuery))
        {
            if (size.next()){
                return size.getInt("sumOfSizes");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /*The block below is for testing the methods
    public static void main(String[] args) {

        LotsDAO lo = new LotsDAO();
        System.out.println(lo.totalWeight());
        System.out.println(lo.totalSize());

    }
    /*/
}
