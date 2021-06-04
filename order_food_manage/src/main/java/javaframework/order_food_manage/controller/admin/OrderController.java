package javaframework.order_food_manage.controller.admin;

import javaframework.order_food_manage.constant.SystemConstant;
import javaframework.order_food_manage.dto.OrderDTO;
import javaframework.order_food_manage.dto.RoleDTO;
import javaframework.order_food_manage.dto.UserDTO;
import javaframework.order_food_manage.service.impl.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class OrderController implements IAdminController<OrderDTO> {

    @Autowired
    private OrderService orderService;

    @GetMapping("/order")
    @Override
    public String showList(Model model, HttpSession session,
                           @RequestParam(defaultValue = "1") int page,
                           @RequestParam(defaultValue = "5") int limit,
                           @RequestParam(defaultValue = "") String search) {
        OrderDTO orderDTO = orderService.getAllPagination(page, limit, search, true);
        model.addAttribute("orderDTO", orderDTO);
        session.setAttribute(SystemConstant.SESSION_ACTIVE,"order");
        return "views/admin/order/order_management";
    }

    @GetMapping(value={"/order/create","/order/{id}"})
    @Override
    public String showEditOrAddPage(Model model, @PathVariable(name = "id", required = false) Long id) {
        OrderDTO orderDTO = new OrderDTO();
        if( id != null ) {
            orderDTO = orderService.findOne(id);
        }
        model.addAttribute("orderDTO",orderDTO);
        return "views/admin/order/editOrder";
    }

    @GetMapping("/order/detail/{id}")
    @Override
    public String showDetail(Model model, @PathVariable(name = "id", required = false) Long id) {
        OrderDTO orderDTO = new OrderDTO();
        if( id != null ) {
            orderDTO = orderService.findOne(id);
        }
        model.addAttribute("orderDTO",orderDTO);
        return "views/admin/order/detailOrder";
    }

    @PutMapping("/order/{id}")
    @ResponseBody
    public OrderDTO updateOrder(HttpSession session, @PathVariable(name = "id") Long id){
        OrderDTO orderDTO = orderService.findOne(id);
        orderDTO.setOrderStatus(1);
        orderDTO.setPaymentMethods(1);
        orderDTO.setUser_manager((UserDTO) session.getAttribute("userDTO"));
        return orderService.save(orderDTO);
    }
}
