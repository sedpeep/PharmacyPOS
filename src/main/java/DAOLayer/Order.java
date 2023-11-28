package DAOLayer;

import java.sql.Timestamp;

public class Order {
    private int orderId;
    private int userId;
    private double total;
    private Timestamp timestamp;


    public Order(int orderId, int userId,Timestamp timestamp, double total) {
        this.orderId = orderId;
        this.userId = userId;
        this.total = total;
        this.timestamp=timestamp;
    }


    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
