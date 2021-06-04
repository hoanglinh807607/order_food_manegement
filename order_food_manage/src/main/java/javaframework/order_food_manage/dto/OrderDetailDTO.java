package javaframework.order_food_manage.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderDetailDTO extends AbstractDTO<OrderDetailDTO>{
    private Long price;
    private Integer quantity;
    private Long subTotal;
    private FoodDTO foodDTO;
    private Long orderId;

    public OrderDetailDTO(FoodDTO foodDTO){
        this.foodDTO = foodDTO;
        this.quantity = 1;
        this.subTotal = foodDTO.getPrice();
        if( foodDTO.getPricePromotion() != 0 ){
            this.price = foodDTO.getPricePromotion();
        }
        else this.price = foodDTO.getPrice();
    }

    public Long getSubTotal() {
        this.subTotal = this.price*this.quantity;
        return subTotal;
    }
}
