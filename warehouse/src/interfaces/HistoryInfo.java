package interfaces;

import java.sql.SQLException;

public interface HistoryInfo {

    void importOrExport(String product, int quantity, String operation) throws SQLException;
}
