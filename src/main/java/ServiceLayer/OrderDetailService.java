package ServiceLayer;


import DAOLayer.OrderDetail;
import DAOLayer.OrderDetailDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class OrderDetailService {
    OrderDetailDAO orderDetailDAO;
    public OrderDetailService(Connection connection){
        orderDetailDAO = new OrderDetailDAO(connection);
    }
    public boolean addOrderDetail(int orderId, int productId, int quantity, double price){
        return orderDetailDAO.addOrderDetail(orderId,productId,quantity,price);
    }
    public boolean updateOrderDetail(int orderDetailId, int quantity, double price){
        return orderDetailDAO.updateOrderDetail(orderDetailId,quantity,price);
    }
    public boolean deleteOrderDetail(int orderDetailId){
        return orderDetailDAO.deleteOrderDetail(orderDetailId);
    }
    public OrderDetail getOrderDetailById(int orderDetailId){
        return orderDetailDAO.getOrderDetailById(orderDetailId);
    }
    public List<OrderDetail> getAllOrderDetails(){
        return orderDetailDAO.getAllOrderDetails();
    }
    public Map<String, Double> getSalesSummaryByWeek() throws SQLException{
        return orderDetailDAO.getSalesSummaryByWeek();
    }
    public Map<String, Double> getSalesSummaryByMonth() throws SQLException{
        return orderDetailDAO.getSalesSummaryByMonth();
    }
    public Map<String, Double> getSalesSummaryByYear() throws SQLException{
        return orderDetailDAO.getSalesSummaryByYear();
    }

}

