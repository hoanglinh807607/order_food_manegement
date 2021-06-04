package javaframework.order_food_manage.controller.admin.api;

import javaframework.order_food_manage.dto.FoodDTO;
import javaframework.order_food_manage.dto.UserDTO;
import javaframework.order_food_manage.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/api")
public class FoodAPI implements IAdminAPI<FoodDTO> {

    @Autowired
    private FoodService foodService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private FoodGroupService foodGroupService;

    @PostMapping(value = "/food")
    @Override
    public String doAdd(Model model, @ModelAttribute FoodDTO foodDTO) {
        model.addAttribute("foodDTO", foodService.save(foodDTO));
        model.addAttribute("categoryList",categoryService.findAll());
        model.addAttribute("foodGroupList", foodGroupService.findAll());
        return "views/admin/food/editFood";
    }

    @PutMapping(value = "/food")
    @Override
    public String doEdit(Model model, @ModelAttribute FoodDTO foodDTO) {
        model.addAttribute("foodDTO", foodService.save(foodDTO));
        model.addAttribute("categoryList",categoryService.findAll());
        model.addAttribute("foodGroupList", foodGroupService.findAll());
        return "views/admin/food/editFood";
    }

    @DeleteMapping(value="/food")
    @Override
    public String doDelete(@RequestBody FoodDTO foodDTO) {
        foodService.delete(foodDTO.getIds());
        return "success";
    }
}
