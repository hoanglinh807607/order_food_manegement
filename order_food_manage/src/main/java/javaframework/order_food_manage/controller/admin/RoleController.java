package javaframework.order_food_manage.controller.admin;

import javaframework.order_food_manage.constant.SystemConstant;
import javaframework.order_food_manage.dto.FoodDTO;
import javaframework.order_food_manage.dto.RoleDTO;
import javaframework.order_food_manage.dto.UserDTO;
import javaframework.order_food_manage.service.impl.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class RoleController implements IAdminController<RoleDTO>{

    @Autowired
    private RoleService roleService;

    @GetMapping("/role")
    @Override
    public String showList(Model model, HttpSession session,
                           @RequestParam(defaultValue = "1") int page,
                           @RequestParam(defaultValue = "5") int limit,
                           @RequestParam(defaultValue = "") String search) {
        RoleDTO roleDTO = roleService.getAllPagination(page, limit, search, true);
        model.addAttribute("roleDTO", roleDTO);
        session.setAttribute(SystemConstant.SESSION_ACTIVE,"role");
        return "views/admin/role/role_management";
    }

    @GetMapping(value={"/role/create","/role/{id}"})
    @Override
    public String showEditOrAddPage(Model model, @PathVariable(name = "id", required = false) Long id) {
        RoleDTO roleDTO = new RoleDTO();
        if( id != null ) {
            roleDTO = roleService.findOne(id);
        }
        model.addAttribute("roleDTO",roleDTO);
        return "views/admin/role/editRole";
    }

    @GetMapping(value="/role/detail/{id}")
    @Override
    public String showDetail(Model model, @PathVariable(name = "id", required = false) Long id) {
        RoleDTO roleDTO = new RoleDTO();
        if( id != null ) {
            roleDTO = roleService.findOne(id);
        }
        model.addAttribute("roleDTO",roleDTO);
        return "views/admin/role/detailRole";
    }
}
