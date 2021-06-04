package javaframework.order_food_manage.controller.admin.api;

import javaframework.order_food_manage.dto.CategoryDTO;
import javaframework.order_food_manage.dto.UserDTO;
import javaframework.order_food_manage.service.impl.CategoryService;
import javaframework.order_food_manage.service.impl.RoleService;
import javaframework.order_food_manage.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/api")
public class CategoryAPI implements IAdminAPI<CategoryDTO> {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/category")
    @Override
    public String doAdd(Model model, @ModelAttribute CategoryDTO obj) {
        model.addAttribute("categoryDTO", categoryService.save(obj));
        return "views/admin/category/editCategory";
    }

    @PutMapping("/category")
    @Override
    public String doEdit(Model model, @ModelAttribute CategoryDTO obj) {
        model.addAttribute("categoryDTO", categoryService.save(obj));
        return "views/admin/category/editCategory";
    }

    @DeleteMapping("/category")
    @Override
    @ResponseBody
    public String doDelete(@RequestBody CategoryDTO obj) {
        categoryService.delete(obj.getIds());
        return "success";
    }
}
