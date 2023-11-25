package ServiceLayer;
import DAOLayer.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class OrderService {
    OrderDAO orderDAO;
    public OrderService(Connection connection){
        orderDAO = new OrderDAO(connection);
    }
    public int addOrder(int userId, double total) throws SQLException {
        return  orderDAO.addOrder(userId,total);
    }
    public boolean updateOrder(int orderId, double total){
        return  orderDAO.updateOrder(orderId,total);
    }
    public boolean deleteOrder(int orderId){
        return orderDAO.deleteOrder(orderId);
    }
    public Order getOrderById(int orderID){
        return orderDAO.getOrderById(orderID);
    }
    public List<Order> getAllOrders(){
        return orderDAO.getAllOrders();
    }
}
