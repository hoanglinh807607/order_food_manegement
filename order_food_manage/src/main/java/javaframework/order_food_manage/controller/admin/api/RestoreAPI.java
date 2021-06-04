package javaframework.order_food_manage.controller.admin.api;

import javaframework.order_food_manage.dto.AbstractDTO;
import javaframework.order_food_manage.dto.UserDTO;
import javaframework.order_food_manage.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/api")
public class RestoreAPI {

    @Autowired ContactService contactService;
    @Autowired CategoryService categoryService;
    @Autowired FoodService foodService;
    @Autowired FoodGroupService foodGroupService;
    @Autowired ImageService imageService;
    @Autowired OrderService orderService;
    @Autowired RoleService roleService;
    @Autowired UserService userService;

    @PostMapping(value = "/restore", consumes = "application/json", produces = "application/json")
    public String showTable(Model model, @RequestBody AbstractDTO abstractDTO) {
        String url = null;
        switch (abstractDTO.getTable()){
            case "1": abstractDTO = foodService.getAllPagination(1,1,abstractDTO.getSearch(),false); url = "views/admin/restore/table_food"; break;
            case "2": abstractDTO = categoryService.getAllPagination(1,1,abstractDTO.getSearch(),false); url = "views/admin/restore/table_category"; break;
            case "3": abstractDTO = imageService.getAllPagination(1,1,abstractDTO.getSearch(),false); url = "views/admin/restore/table_image"; break;
            case "4": abstractDTO = foodGroupService.getAllPagination(1,1,abstractDTO.getSearch(),false); url = "views/admin/restore/table_group"; break;
            case "5": abstractDTO = orderService.getAllPagination(1,1,abstractDTO.getSearch(),false); url = "views/admin/restore/table_order"; break;
            case "6": abstractDTO = userService.getAllPagination(1,1,abstractDTO.getSearch(),false); url = "views/admin/restore/table_user"; break;
            case "7": abstractDTO = roleService.getAllPagination(1,1,abstractDTO.getSearch(),false); url = "views/admin/restore/table_role"; break;
            case "8": abstractDTO = contactService.getAllPagination(1,1,abstractDTO.getSearch(),false); url = "views/admin/restore/table_contact"; break;
        }
        model.addAttribute("dto",abstractDTO);
        return url;
    }

    @PutMapping(value = "/restore", consumes = "application/json", produces = "application/json")
    public AbstractDTO restoreData(@RequestBody AbstractDTO abstractDTO) {
        switch (abstractDTO.getTable()){
            case "1": foodService.restore(abstractDTO.getIds()); break;
            case "2": categoryService.restore(abstractDTO.getIds()); break;
            case "3": imageService.restore(abstractDTO.getIds()); break;
            case "4": foodGroupService.restore(abstractDTO.getIds()); break;
            case "5": orderService.restore(abstractDTO.getIds()); break;
            case "6": userService.restore(abstractDTO.getIds()); break;
            case "7": roleService.restore(abstractDTO.getIds()); break;
            case "8": contactService.restore(abstractDTO.getIds()); break;
        }
        return abstractDTO;
    }

    @DeleteMapping(value = "/restore", consumes = "application/json", produces = "application/json")
    public AbstractDTO doDelete(@RequestBody AbstractDTO abstractDTO) {
        switch (abstractDTO.getTable()){
            case "1": foodService.permanentDelete(abstractDTO.getIds()); break;
            case "2": categoryService.permanentDelete(abstractDTO.getIds()); break;
            case "3": imageService.permanentDelete(abstractDTO.getIds()); break;
            case "4": foodGroupService.permanentDelete(abstractDTO.getIds()); break;
            case "5": orderService.permanentDelete(abstractDTO.getIds()); break;
            case "6": userService.permanentDelete(abstractDTO.getIds()); break;
            case "7": roleService.permanentDelete(abstractDTO.getIds()); break;
            case "8": contactService.permanentDelete(abstractDTO.getIds()); break;
        }
        return abstractDTO;
    }
}
