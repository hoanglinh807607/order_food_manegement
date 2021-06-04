package javaframework.order_food_manage.dto;

import lombok.Data;

@Data
public class ContactDTO extends AbstractDTO<ContactDTO>{
    private String email;
    private String fullName;
    private String phone;
    private String title;
    private String Content;
    private Integer state;
}
