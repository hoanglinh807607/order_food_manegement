package javaframework.order_food_manage.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserDTO extends AbstractDTO<UserDTO> {
    private String username;
    private String password;
    private String fullName;
    private String phone;
    private String address;
    private List<Long> roleId = new ArrayList<>();
    private List<String> roleName = new ArrayList<>();
}
