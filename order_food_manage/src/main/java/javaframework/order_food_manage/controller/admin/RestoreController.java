package javaframework.order_food_manage.controller.admin;

import javaframework.order_food_manage.constant.SystemConstant;
import javaframework.order_food_manage.dto.AbstractDTO;
import javaframework.order_food_manage.dto.ContactDTO;
import javaframework.order_food_manage.dto.FoodDTO;
import javaframework.order_food_manage.dto.OrderDetailDTO;
import javaframework.order_food_manage.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class RestoreController{

    @Autowired ContactService contactService;
    @Autowired CategoryService categoryService;
    @Autowired FoodService foodService;
    @Autowired FoodGroupService foodGroupService;
    @Autowired ImageService imageService;
    @Autowired OrderService orderService;
    @Autowired RoleService roleService;
    @Autowired UserService userService;

    @GetMapping("/restore")
    public String showList(Model model, HttpSession session,
                           @RequestParam(name = "table", defaultValue = "") String table) {
        AbstractDTO abstractDTO = new AbstractDTO();
        abstractDTO.setTable(table);
        model.addAttribute("listTableName", new ArrayList<>(Arrays.asList("food","category","image","group","order","user","role","contact")));
        model.addAttribute("abstractDTO",abstractDTO);
        session.setAttribute(SystemConstant.SESSION_ACTIVE,"restore");
        return "views/admin/restore/restore_management";
    }

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

}
