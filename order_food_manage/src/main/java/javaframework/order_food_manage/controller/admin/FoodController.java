package javaframework.order_food_manage.controller.admin;

import javaframework.order_food_manage.constant.SystemConstant;
import javaframework.order_food_manage.dto.CategoryDTO;
import javaframework.order_food_manage.dto.FoodDTO;
import javaframework.order_food_manage.service.impl.CategoryService;
import javaframework.order_food_manage.service.impl.FoodGroupService;
import javaframework.order_food_manage.service.impl.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class FoodController implements IAdminController<FoodDTO> {

    @Autowired
    private FoodService foodService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private FoodGroupService foodGroupService;

    @GetMapping("/food")
    @Override
    public String showList(Model model, HttpSession session,
                           @RequestParam(defaultValue = "1") int page,
                           @RequestParam(defaultValue = "5") int limit,
                           @RequestParam(defaultValue = "") String search) {
        FoodDTO foodDTO = foodService.getAllPagination(page, limit, search,true);
        model.addAttribute("foodDTO", foodDTO);
        session.setAttribute(SystemConstant.SESSION_ACTIVE,"food");
        session.setAttribute(SystemConstant.SESSION_AUTHORITY, SecurityContextHolder.getContext().getAuthentication());
        return "views/admin/food/food_management";
    }

    @GetMapping(value={"/food/create","/food/{id}"})
    @Override
    public String showEditOrAddPage(Model model, @PathVariable(name = "id", required = false) Long id) {
        FoodDTO foodDTO = new FoodDTO();
        if( id != null ) {
            foodDTO = foodService.findOne(id);
        }
        model.addAttribute("foodDTO",foodDTO);
        model.addAttribute("categoryList",categoryService.findAll());
        model.addAttribute("foodGroupList", foodGroupService.findAll());
        return "views/admin/food/editFood";
    }

    @GetMapping(value={"/food/detail/{id}"})
    @Override
    public String showDetail(Model model, @PathVariable(name = "id", required = false) Long id) {
        FoodDTO dto = new FoodDTO();
        if( id != null ) {
            dto = foodService.findOne(id);
        }
        model.addAttribute("foodDTO",dto);
        return "views/admin/food/detailFood";
    }

}
