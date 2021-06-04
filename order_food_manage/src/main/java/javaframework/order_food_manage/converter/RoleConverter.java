package javaframework.order_food_manage.converter;

import javaframework.order_food_manage.dto.RoleDTO;
import javaframework.order_food_manage.entities.RoleEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class RoleConverter extends AbstractConverter<RoleDTO> implements IAbstractConverter<RoleDTO, RoleEntity>{

    @Override
    public RoleDTO toDto(RoleEntity entity) {
        RoleDTO dto = new RoleDTO();
        dto.setCode(entity.getCode());
        dto.setName(entity.getName());
        return toDto(dto, entity);
    }

    @Override
    public RoleEntity toEntity(RoleDTO dto) {
        RoleEntity entity = new RoleEntity();
        return getRoleEntity(entity,dto);
    }

    @Override
    public RoleEntity toEntity(RoleEntity entity, RoleDTO dto) {
        return getRoleEntity(entity,dto);
    }

    @NotNull
    private RoleEntity getRoleEntity(RoleEntity entity, RoleDTO dto) {
        entity.setCode(dto.getCode());
        entity.setName(dto.getName());
        entity.setStatus( dto.getStatus() != null ? dto.getStatus() : true);
        return entity;
    }

}
