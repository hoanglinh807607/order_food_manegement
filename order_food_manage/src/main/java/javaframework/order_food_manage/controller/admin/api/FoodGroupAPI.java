package javaframework.order_food_manage.controller.admin.api;

import javaframework.order_food_manage.dto.FoodGroupDTO;
import javaframework.order_food_manage.dto.UserDTO;
import javaframework.order_food_manage.service.impl.FoodGroupService;
import javaframework.order_food_manage.service.impl.RoleService;
import javaframework.order_food_manage.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/api")
public class FoodGroupAPI implements IAdminAPI<FoodGroupDTO> {

    @Autowired
    private FoodGroupService foodGroupService;

    @PostMapping("/group")
    @Override
    public String doAdd(Model model, @ModelAttribute FoodGroupDTO obj) {
        model.addAttribute("groupDTO", foodGroupService.save(obj));
        return "views/admin/group/editGroup";
    }

    @PutMapping("/group")
    @Override
    public String doEdit(Model model, @ModelAttribute FoodGroupDTO obj) {
        model.addAttribute("groupDTO", foodGroupService.save(obj));
        return "views/admin/group/editGroup";
    }

    @DeleteMapping("/group")
    @Override
    public String doDelete(@RequestBody FoodGroupDTO obj) {
        foodGroupService.delete(obj.getIds());
        return "success";
    }
}
