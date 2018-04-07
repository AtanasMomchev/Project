package dao;

import interfaces.LotsInfo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LotsDAO extends AbstractDAO implements LotsInfo {


    public List<Integer> getLotIds() throws SQLException {
        String getLotIdQuery = "SELECT idLots FROM warehouse.lots;";
        List<Integer> lotIds = new ArrayList<>();

        try (Connection con = getConnection();
             Statement getLotId = con.createStatement();
             ResultSet lotId = getLotId.executeQuery(getLotIdQuery)
        ){
            while (lotId.next()){
                lotIds.add(lotId.getInt(1));
            }
        }
        return lotIds;
    }


    public int getLotSize(int lot_id) throws SQLException {

        String getLotSizeQuery = "SELECT sizeLots FROM warehouse.lots " +
                "WHERE idLots = ?;";

        try (Connection con = getConnection();
             PreparedStatement getLotSize = con.prepareStatement(getLotSizeQuery);
             ResultSet size = getSize(getLotSize, lot_id)
        ){
            if (size.next()){
                return size.getInt(1);
            }
        }
        return -1;
    }


    public double getLotWeight(int lot_id) throws SQLException {

        String getLotWeightQuery = "SELECT weightCapacityLots FROM warehouse.lots " +
                "WHERE idLots = ?;";

        try (Connection con = getConnection();
             PreparedStatement getLotSize = con.prepareStatement(getLotWeightQuery);
             ResultSet size = getWeight(getLotSize, lot_id)
        ){
            if (size.next()){
                return size.getDouble(1);
            }
        }
        return -1;
    }


    @Override
    public double totalWeight() throws SQLException {

        String getWeightQuery = "SELECT SUM(lots.weightCapacityLots)\n" +
                "AS sumOfWeights\n" +
                "FROM warehouse.lots;\n";
        try (Connection con = getConnection();
             Statement getWeight = con.createStatement();
             ResultSet weight = getWeight.executeQuery(getWeightQuery)){

            if (weight.next()) {
                return weight.getDouble("sumOfWeights");
            }

        }
        return 0;
    }

    @Override
    public int totalSize() throws SQLException {
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
        }
        return 0;
    }

    private ResultSet getSize(PreparedStatement ps, int id) throws SQLException {

        ps.setInt(1, id);
        return ps.executeQuery();
    }

    private ResultSet getWeight(PreparedStatement ps, int id) throws SQLException {

        ps.setDouble(1, id);
        return ps.executeQuery();
    }


//    /*The block below is for testing the methods
    public static void main(String[] args) throws SQLException {

        LotsDAO lo = new LotsDAO();
//        System.out.println(lo.totalWeight());
//        System.out.println(lo.totalSize());
//        System.out.println(lo.getLotSize(1));
        System.out.println(lo.getLotWeight(1));
    }
//    /*/
}
