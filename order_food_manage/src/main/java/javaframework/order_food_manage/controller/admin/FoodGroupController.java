package javaframework.order_food_manage.controller.admin;

import javaframework.order_food_manage.constant.SystemConstant;
import javaframework.order_food_manage.dto.FoodGroupDTO;
import javaframework.order_food_manage.dto.OrderDTO;
import javaframework.order_food_manage.service.impl.FoodGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class FoodGroupController implements IAdminController<FoodGroupDTO> {

    @Autowired
    private FoodGroupService foodGroupService;

    @GetMapping("/group")
    @Override
    public String showList(Model model, HttpSession session,
                           @RequestParam(defaultValue = "1") int page,
                           @RequestParam(defaultValue = "5") int limit,
                           @RequestParam(defaultValue = "") String search) {
        FoodGroupDTO foodGroupDTO = foodGroupService.getAllPagination(page, limit, search,true);
        model.addAttribute("groupDTO", foodGroupDTO);
        session.setAttribute(SystemConstant.SESSION_ACTIVE,"group");
        return "views/admin/group/group_management";
    }

    @GetMapping(value={"/group/create","/group/{id}"})
    @Override
    public String showEditOrAddPage(Model model, @PathVariable(name = "id", required = false) Long id) {
        FoodGroupDTO foodGroupDTO = new FoodGroupDTO();
        if( id != null ) {
            foodGroupDTO = foodGroupService.findOne(id);
        }
        model.addAttribute("foodGroupDTO",foodGroupDTO);
        return "views/admin/group/editGroup";
    }

    @GetMapping("/group/detail/{id}")
    @Override
    public String showDetail(Model model, @PathVariable(name = "id", required = false) Long id) {
        FoodGroupDTO foodGroupDTO = new FoodGroupDTO();
        if( id != null ) {
            foodGroupDTO = foodGroupService.findOne(id);
        }
        model.addAttribute("foodGroupDTO",foodGroupDTO);
        return "views/admin/group/detailGroup";
    }
}
