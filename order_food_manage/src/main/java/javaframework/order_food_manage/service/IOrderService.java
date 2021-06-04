package javaframework.order_food_manage.service;

import javaframework.order_food_manage.dto.OrderDTO;

import javax.servlet.http.HttpSession;

public interface IOrderService extends GenericService<OrderDTO> {
    OrderDTO getOrderDto(HttpSession session);
    void setOrderDto(HttpSession session, OrderDTO orderDTO);
    void removeOrderDTO(HttpSession session);
    void setTotalQuantity(HttpSession session, OrderDTO orderDTO);
}
