package javaframework.order_food_manage.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FoodDTO extends AbstractDTO<FoodDTO> {
    private String name;
    private Long price;
    private Long pricePromotion;
    private String description;
    private Long categoryId;
    private Long foodGroupId;
    private String categoryName;
    private String foodGroupName;
    private List<ImageDTO> imageDTOS = new ArrayList<>();
}
