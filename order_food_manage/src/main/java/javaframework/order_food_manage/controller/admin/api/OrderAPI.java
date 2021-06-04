package javaframework.order_food_manage.controller.admin.api;

import javaframework.order_food_manage.dto.OrderDTO;
import javaframework.order_food_manage.dto.UserDTO;
import javaframework.order_food_manage.service.impl.OrderService;
import javaframework.order_food_manage.service.impl.RoleService;
import javaframework.order_food_manage.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/api")
public class OrderAPI implements IAdminAPI<OrderDTO> {

    @Autowired
    private OrderService orderService;


    @PostMapping("/order")
    @Override
    public String doAdd(Model model, @ModelAttribute OrderDTO obj) {
        model.addAttribute("roleDTO", orderService.save(obj));
        return "views/admin/order/editOrder";
    }

    @PutMapping("/order")
    @Override
    public String doEdit(Model model, @ModelAttribute OrderDTO obj) {
        model.addAttribute("orderDTO", orderService.save(obj));
        return "views/admin/order/editOrder";
    }

    @DeleteMapping("/order")
    @Override
    public String doDelete(@RequestBody OrderDTO obj) {
        orderService.delete(obj.getIds());
        return "success";
    }
}
