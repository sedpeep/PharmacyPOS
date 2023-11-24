import java.sql.Connection;
import java.util.List;

public class OrderService {
    OrderDAO orderDAO;
    public OrderService(Connection connection){
        orderDAO = new OrderDAO(connection);
    }
    public boolean addOrder(int userId, double total) {
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
