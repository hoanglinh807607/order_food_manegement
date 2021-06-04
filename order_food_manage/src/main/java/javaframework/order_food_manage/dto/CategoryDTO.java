package javaframework.order_food_manage.dto;

import lombok.Data;

@Data
public class CategoryDTO extends AbstractDTO<CategoryDTO>{
    private String code;
    private String name;
}
