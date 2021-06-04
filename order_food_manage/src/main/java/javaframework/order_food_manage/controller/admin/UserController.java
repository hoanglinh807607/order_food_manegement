package javaframework.order_food_manage.controller.admin;

import javaframework.order_food_manage.constant.SystemConstant;
import javaframework.order_food_manage.dto.UserDTO;
import javaframework.order_food_manage.service.impl.RoleService;
import javaframework.order_food_manage.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class UserController implements IAdminController<UserDTO>{

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @GetMapping("/user")
    @Override
    public String showList(Model model, HttpSession session,
                           @RequestParam(defaultValue = "1") int page,
                           @RequestParam(defaultValue = "5") int limit,
                           @RequestParam(defaultValue = "") String search) {
        UserDTO userDTO = userService.getAllPagination(page, limit, search,true);
        model.addAttribute("userDTO", userDTO);
        session.setAttribute(SystemConstant.SESSION_ACTIVE,"user");
        session.setAttribute(SystemConstant.SESSION_AUTHORITY,SecurityContextHolder.getContext().getAuthentication());
        return "views/admin/user/user_management";
    }

    @GetMapping(value={"/user/create","/user/{id}"})
    @Override
    public String showEditOrAddPage(Model model, @PathVariable(name = "id", required = false) Long id) {
        UserDTO userDTO = new UserDTO();
        if( id != null ) {
            userDTO = userService.findOne(id);
        }
        model.addAttribute("userDTO",userDTO);
        model.addAttribute("roleList",roleService.findAll());
        return "views/admin/user/editUser";
    }

    @GetMapping("/user/detail/{id}")
    @Override
    public String showDetail(Model model,  @PathVariable(name = "id", required = false) Long id) {
        UserDTO userDTO = new UserDTO();
        if( id != null ) {
            userDTO = userService.findOne(id);
        }
        model.addAttribute("userDTO",userDTO);
        model.addAttribute("roleList",roleService.findAll());
        return "views/admin/user/detailUser";
    }
}
