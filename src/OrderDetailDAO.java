import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OrderDetailDAO {
    private Connection connection;

    public OrderDetailDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean addOrderDetail(int orderId, int productId, int quantity, double price) {
        String sql = "INSERT INTO OrderDetails (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, orderId);
            pstmt.setInt(2, productId);
            pstmt.setInt(3, quantity);
            pstmt.setDouble(4, price);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean updateOrderDetail(int orderDetailId, int quantity, double price) {
        String sql = "UPDATE OrderDetails SET quantity = ?, price = ? WHERE order_detail_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, quantity);
            pstmt.setDouble(2, price);
            pstmt.setInt(3, orderDetailId);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteOrderDetail(int orderDetailId) {
        String sql = "DELETE FROM OrderDetails WHERE order_detail_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, orderDetailId);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public OrderDetail getOrderDetailById(int orderDetailId) {
        String sql = "SELECT * FROM OrderDetails WHERE order_detail_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, orderDetailId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new OrderDetail(
                        rs.getInt("order_detail_id"),
                        rs.getInt("order_id"),
                        rs.getInt("product_id"),
                        rs.getInt("quantity"),
                        rs.getDouble("price")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<OrderDetail> getAllOrderDetails() {
        List<OrderDetail> orderDetails = new ArrayList<>();
        String sql = "SELECT * FROM OrderDetails";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                orderDetails.add(new OrderDetail(
                        rs.getInt("order_detail_id"),
                        rs.getInt("order_id"),
                        rs.getInt("product_id"),
                        rs.getInt("quantity"),
                        rs.getDouble("price")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderDetails;
    }
    public Map<String, Double> getSalesSummaryByWeek() throws SQLException {
        Map<String, Double> salesByWeek = new LinkedHashMap<>();
        String sql = "SELECT WEEK(o.timestamp) as week_number, SUM(od.quantity * od.price) as weekly_sales " +
                "FROM Orders o " +
                "JOIN OrderDetails od ON o.order_id = od.order_id " +
                "GROUP BY week_number " +
                "ORDER BY week_number ASC";

        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String weekNumber = "Week " + rs.getInt("week_number");
                Double weeklySales = rs.getDouble("weekly_sales");
                salesByWeek.put(weekNumber, weeklySales);
            }
        }
        return salesByWeek;
    }
    public Map<String, Double> getSalesSummaryByMonth() throws SQLException {
        Map<String, Double> salesByMonth = new LinkedHashMap<>();
        String sql = "SELECT MONTH(o.timestamp) as month_number, SUM(od.quantity * od.price) as monthly_sales " +
                "FROM Orders o " +
                "JOIN OrderDetails od ON o.order_id = od.order_id " +
                "GROUP BY month_number " +
                "ORDER BY month_number ASC";

        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String monthNumber = "Month " + rs.getInt("month_number");
                Double monthlySales = rs.getDouble("monthly_sales");
                salesByMonth.put(monthNumber, monthlySales);
            }
        }
        return salesByMonth;
    }
    public Map<String, Double> getSalesSummaryByYear() throws SQLException {
        Map<String, Double> salesByYear = new LinkedHashMap<>();
        String sql = "SELECT YEAR(o.timestamp) as year_number, SUM(od.quantity * od.price) as year_sales " +
                "FROM Orders o " +
                "JOIN OrderDetails od ON o.order_id = od.order_id " +
                "GROUP BY year_number " +
                "ORDER BY year_number ASC";

        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String yearNumber = "Year " + rs.getInt("year_number");
                Double yearSales = rs.getDouble("year_sales");
                salesByYear.put(yearNumber, yearSales);
            }
        }
        return salesByYear;
    }

}
class OrderDetail {
    private int orderDetailId;
    private int orderId;
    private int productId;
    private int quantity;
    private double price;

    // Constructor
    public OrderDetail(int orderDetailId, int orderId, int productId, int quantity, double price) {
        this.orderDetailId = orderDetailId;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    // Getters and Setters
    public int getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(int orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

