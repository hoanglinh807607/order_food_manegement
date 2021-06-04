package javaframework.order_food_manage.controller.client.api;

import javaframework.order_food_manage.dto.FoodDTO;
import javaframework.order_food_manage.dto.OrderDTO;
import javaframework.order_food_manage.dto.UserDTO;
import javaframework.order_food_manage.service.impl.FoodService;
import javaframework.order_food_manage.service.impl.OrderService;
import javaframework.order_food_manage.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RequestMapping("/cart")
@RestController
public class APIShoppingCart {

    @Autowired
    private FoodService foodService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/add")
    @ResponseBody
    public OrderDTO addToCart(HttpSession session, @RequestParam("id") Long id,
                                    @RequestParam(value = "quantity", required = false, defaultValue = "1") Integer quantity){
        FoodDTO foodDTO = foodService.findOne(id);
        // Create OrderDTO and add into session
        OrderDTO orderDTO = orderService.getOrderDto(session);
        // Order add OrderDetail with FoodDTO and quantity
        orderDTO.addOrderDetailDto(foodDTO, quantity);
        orderService.setOrderDto(session,orderDTO);
        return orderDTO;
    }

    @DeleteMapping(value = "/remove")
    @ResponseBody
    public OrderDTO removeOrderDetailInOrder(HttpSession session, @RequestParam("id") Long id){
        FoodDTO foodDTO = foodService.findOne(id);
        // Create OrderDTO and add into session
        OrderDTO orderDTO = orderService.getOrderDto(session);
        // Order remove OrderDetail with FoodDTO
        orderDTO.removeOrderDetail(foodDTO);
        orderService.setOrderDto(session,orderDTO);
        return orderDTO;
    }

    @PutMapping(value = "/update")
    @ResponseBody
    public OrderDTO updateQuantityOrder(HttpSession session, @RequestParam("id") Long id,
                             @RequestParam(value = "quantity", required = false, defaultValue = "1") Integer quantity){
        FoodDTO foodDTO = foodService.findOne(id);
        // Create OrderDTO and add into session
        OrderDTO orderDTO = orderService.getOrderDto(session);
        // Update OrderDetail in Order with FoodDTO and quantity update
        orderDTO.updateQuantityInOrderDetail(foodDTO,quantity);
        orderService.setOrderDto(session,orderDTO);
        return orderDTO;
    }

    @PostMapping("/pay")
    @ResponseBody
    public OrderDTO informPay(HttpSession session,
                            @RequestBody UserDTO userDTO,
                            @RequestParam(name="paymentMethods") Integer paymentMethods) {
        OrderDTO orderDTO = orderService.getOrderDto(session);
        orderDTO.setPaymentMethods(paymentMethods);
        orderDTO.setAddress(userDTO.getAddress());
        orderDTO.setUser_customer((UserDTO) session.getAttribute("userDTO"));
        if( !orderDTO.getUser_customer().getPhone().equals(userDTO.getPhone())){
            orderDTO.getUser_customer().setPhone(userDTO.getPhone());
            orderDTO.setUser_customer(userService.save(orderDTO.getUser_customer()));
        }
        orderDTO = orderService.save(orderDTO);
        orderDTO.clear();
        orderService.setOrderDto(session,orderDTO);
        return orderDTO;
    }

}
