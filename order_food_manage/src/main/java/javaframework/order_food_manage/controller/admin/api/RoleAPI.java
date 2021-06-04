package javaframework.order_food_manage.controller.admin.api;

import javaframework.order_food_manage.dto.RoleDTO;
import javaframework.order_food_manage.dto.UserDTO;
import javaframework.order_food_manage.service.impl.RoleService;
import javaframework.order_food_manage.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/api")
public class RoleAPI implements IAdminAPI<RoleDTO> {

    @Autowired
    private RoleService roleService;

    @PostMapping("/role")
    @Override
    public String doAdd(Model model, @ModelAttribute RoleDTO obj) {
        model.addAttribute("roleDTO", roleService.save(obj));
        return "views/admin/role/editRole";
    }

    @PutMapping("/role")
    @Override
    public String doEdit(Model model, @ModelAttribute RoleDTO obj) {
        model.addAttribute("foodDTO", roleService.save(obj));
        return "views/admin/role/editRole";
    }

    @DeleteMapping("/role")
    @Override
    public String doDelete(@RequestBody RoleDTO obj) {
        roleService.delete(obj.getIds());
        return "success";
    }
}
