package exceptions;

public class WarehouseExceptions extends Exception {
    public WarehouseExceptions() {
    }

    public WarehouseExceptions(String message) {
        super(message);
    }

    public WarehouseExceptions(String message, Throwable cause) {
        super(message, cause);
    }

    public WarehouseExceptions(Throwable cause) {
        super(cause);
    }

    public WarehouseExceptions(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
