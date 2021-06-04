package javaframework.order_food_manage.service;


import javaframework.order_food_manage.dto.UserDTO;

import java.util.List;

public interface IUserService extends GenericService<UserDTO>{
    UserDTO findByUserName(String username);
    UserDTO findByUsernameAndStatus(String username, boolean status);
}
