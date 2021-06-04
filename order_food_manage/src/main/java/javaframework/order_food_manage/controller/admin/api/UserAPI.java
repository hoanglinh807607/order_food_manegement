package javaframework.order_food_manage.controller.admin.api;

import javaframework.order_food_manage.dto.UserDTO;
import javaframework.order_food_manage.service.impl.RoleService;
import javaframework.order_food_manage.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/api")
public class UserAPI implements IAdminAPI<UserDTO> {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @PostMapping(value = "/user")
    @Override
    public String doAdd(Model model, @ModelAttribute UserDTO obj) {
        UserDTO userDTO = new UserDTO();
        if( obj.getId() == null ) {
            if( userService.findByUsernameAndStatus(obj.getUsername(),true ) == null ){
                userDTO =  userService.save(obj);
            }else{
                userDTO = obj;
                userDTO.setAlert("danger");
                userDTO.setMessage("Username already exists");
            }
        }else{
            userDTO =  userService.save(obj);
        }
        model.addAttribute("userDTO",userDTO);
        model.addAttribute("roleList",roleService.findAll());
        return "views/admin/user/editUser";
    }

    @PutMapping(value = "/user")
    @Override
    public String doEdit(Model model, @ModelAttribute UserDTO obj) {
        model.addAttribute("userDTO", userService.save(obj));
        model.addAttribute("roleList",roleService.findAll());
        return "views/admin/user/editUser";
    }

    @DeleteMapping(value="/user")
    @Override
    public String doDelete(@RequestBody UserDTO obj) {
        userService.delete(obj.getIds());
        return "success";
    }
}
