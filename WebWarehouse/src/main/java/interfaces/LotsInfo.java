package interfaces;

import java.sql.SQLException;

public interface LotsInfo {

    double totalWeight() throws SQLException;
    int totalSize() throws SQLException;
}
