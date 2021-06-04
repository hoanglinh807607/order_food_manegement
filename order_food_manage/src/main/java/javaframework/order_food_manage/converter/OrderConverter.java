package javaframework.order_food_manage.converter;

import javaframework.order_food_manage.dto.OrderDTO;
import javaframework.order_food_manage.dto.OrderDetailDTO;
import javaframework.order_food_manage.entities.OrderEntity;
import javaframework.order_food_manage.repository.UserRepos;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderConverter extends AbstractConverter<OrderDTO> implements IAbstractConverter<OrderDTO, OrderEntity> {

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private OrderDetailConverter orderDetailConverter;

    @Override
    public OrderDTO toDto(OrderEntity entity) {
        OrderDTO dto = new OrderDTO();
        dto.setCode(entity.getCode());
        dto.setTotal(entity.getTotal());
        dto.setOrderStatus(entity.getOrderStatus());
        dto.setAddress(entity.getAddress());
        dto.setPaymentMethods(entity.getPaymentMethods());
        if( entity.getUserManagerEntity() != null ) dto.setUser_manager(userConverter.toDto(entity.getUserManagerEntity()));
        if( entity.getUserCustomerEntity() != null ) dto.setUser_customer(userConverter.toDto(entity.getUserCustomerEntity()));
        if( entity.getOrderDetailEntities() != null ) entity.getOrderDetailEntities().forEach(orderDetailEntity -> { ;
            dto.getListOrderDetail().add(orderDetailConverter.toDto(orderDetailEntity));
        });
        dto.setTotalQuantityFood();
        return toDto(dto,entity);
    }

    @Override
    public OrderEntity toEntity(OrderDTO dto) {
        OrderEntity entity = new OrderEntity();
        return getOrderEntity(entity,dto);
    }

    @Override
    public OrderEntity toEntity(OrderEntity entity, OrderDTO dto) {
        return getOrderEntity(entity,dto);
    }

    @NotNull
    private OrderEntity getOrderEntity(OrderEntity entity, OrderDTO dto) {
        entity.setCode(dto.getCode());
        entity.setTotal(dto.getTotal());
        entity.setOrderStatus(dto.getOrderStatus());
        entity.setAddress(dto.getAddress());
        entity.setPaymentMethods(dto.getPaymentMethods());
        if( dto.getUser_manager() != null ) entity.setUserManagerEntity(userConverter.toEntity(dto.getUser_manager()));
        if( dto.getUser_customer() != null ) entity.setUserCustomerEntity(userConverter.toEntity(dto.getUser_customer()));
        entity.setStatus( dto.getStatus() != null ? dto.getStatus() : true);
        return entity;
    }
}
