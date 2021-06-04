package javaframework.order_food_manage.controller.client.api;

import javaframework.order_food_manage.dto.UserDTO;
import javaframework.order_food_manage.jwt.JwtUtils;
import javaframework.order_food_manage.service.impl.UserDetailService;
import javaframework.order_food_manage.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiLogin {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailService userDetailService;

    @PostMapping(value = "/register", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public UserDTO insertUser(@RequestBody UserDTO obj) {
        UserDTO userDTO = new UserDTO();
        if (userService.findByUsernameAndStatus(obj.getUsername(), true) == null) {
            obj.getRoleId().add(2l);
            userDTO = userService.save(obj);
        }
        return userDTO;
    }

    @PostMapping(value = "/login", consumes = "application/json", produces = "application/json")
    public String authenticateAdmin(@RequestBody UserDTO userDTO) throws Exception {
        //#Tạo chuỗi authentication từ username và password (object LoginRequest - file này chỉ là 1 class bình thường, chứa 2 trường username và password)
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword()));
        } catch (Exception e) {
            throw new Exception("invalid username/password");
        }
        UserDetails userDetails = userDetailService.loadUserByUsername(userDTO.getUsername());
//        UsernamePasswordAuthenticationToken
//                authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        final String jwt = jwtUtils.generateToken(userDetails);
        return jwt;
    }


}
