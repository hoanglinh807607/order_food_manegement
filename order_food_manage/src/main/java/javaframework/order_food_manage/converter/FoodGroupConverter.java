package javaframework.order_food_manage.converter;

import javaframework.order_food_manage.dto.FoodGroupDTO;
import javaframework.order_food_manage.entities.FoodGroupEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class FoodGroupConverter extends AbstractConverter<FoodGroupDTO> implements IAbstractConverter<FoodGroupDTO, FoodGroupEntity> {
    @Override
    public FoodGroupDTO toDto(FoodGroupEntity entity) {
        FoodGroupDTO dto = new FoodGroupDTO();
        dto.setCode(entity.getCode());
        dto.setName(entity.getName());
        return toDto(dto,entity);
    }

    @Override
    public FoodGroupEntity toEntity(FoodGroupDTO dto) {
        FoodGroupEntity entity = new FoodGroupEntity();
        return getFoodGroupEntity(entity,dto);
    }

    @Override
    public FoodGroupEntity toEntity(FoodGroupEntity entity, FoodGroupDTO dto) {
        return getFoodGroupEntity(entity,dto);
    }

    @NotNull
    private FoodGroupEntity getFoodGroupEntity(FoodGroupEntity entity, FoodGroupDTO dto) {
        entity.setCode(dto.getCode());
        entity.setName(dto.getName());
        entity.setStatus( dto.getStatus() != null ? dto.getStatus() : true);
        return entity;
    }
}
