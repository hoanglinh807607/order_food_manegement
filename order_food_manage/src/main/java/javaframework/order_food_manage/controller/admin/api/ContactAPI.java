package javaframework.order_food_manage.controller.admin.api;

import javaframework.order_food_manage.dto.ContactDTO;
import javaframework.order_food_manage.dto.UserDTO;
import javaframework.order_food_manage.service.impl.ContactService;
import javaframework.order_food_manage.service.impl.RoleService;
import javaframework.order_food_manage.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/api")
public class ContactAPI {

    @Autowired
    private ContactService contactService;

    @DeleteMapping("/contact")
    public String doDelete(@RequestBody ContactDTO obj) {
        contactService.delete(obj.getIds());
        return "success";
    }
}
