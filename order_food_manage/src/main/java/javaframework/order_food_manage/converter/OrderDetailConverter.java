package javaframework.order_food_manage.converter;

import javaframework.order_food_manage.dto.FoodDTO;
import javaframework.order_food_manage.dto.OrderDetailDTO;
import javaframework.order_food_manage.entities.OrderDetailEntity;
import javaframework.order_food_manage.repository.OrderRepos;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderDetailConverter extends AbstractConverter<OrderDetailDTO> implements IAbstractConverter<OrderDetailDTO, OrderDetailEntity> {

    @Autowired
    private OrderRepos orderRepos;

    @Autowired
    private FoodConverter foodConverter;

    @Override
    public OrderDetailDTO toDto(OrderDetailEntity entity) {
        OrderDetailDTO dto = new OrderDetailDTO(foodConverter.toDto(entity.getFoodEntity()));
        dto.setPrice(entity.getPrice());
        dto.setQuantity(entity.getQuantity());
        dto.setSubTotal(entity.getSubTotal());
        dto.setOrderId(entity.getOrderEntity().getId());
        return toDto(dto,entity);
    }

    @Override
    public OrderDetailEntity toEntity(OrderDetailDTO dto) {
        OrderDetailEntity entity = new OrderDetailEntity();
        return getOrderDetailEntity(entity,dto);
    }

    @Override
    public OrderDetailEntity toEntity(OrderDetailEntity entity, OrderDetailDTO dto) {
        return getOrderDetailEntity(entity,dto);
    }

    @NotNull
    private OrderDetailEntity getOrderDetailEntity(OrderDetailEntity entity, OrderDetailDTO dto) {
        entity.setPrice(dto.getPrice());
        entity.setQuantity(dto.getQuantity());
        entity.setSubTotal(dto.getSubTotal());
        entity.setOrderEntity(orderRepos.findById(dto.getOrderId()).get());
        entity.setFoodEntity(foodConverter.toEntity(dto.getFoodDTO()));
        entity.setStatus( dto.getStatus() != null ? dto.getStatus() : true);
        return entity;
    }

}
