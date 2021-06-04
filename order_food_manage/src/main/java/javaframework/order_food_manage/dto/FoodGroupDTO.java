package javaframework.order_food_manage.dto;

import lombok.Data;

@Data
public class FoodGroupDTO extends AbstractDTO<FoodGroupDTO>{
    private String code;
    private String name;
}
