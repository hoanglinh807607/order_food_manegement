package javaframework.order_food_manage.converter;

import javaframework.order_food_manage.dto.UserDTO;
import javaframework.order_food_manage.entities.RoleEntity;
import javaframework.order_food_manage.entities.UserEntity;
import javaframework.order_food_manage.repository.RoleRepos;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserConverter extends AbstractConverter<UserDTO> implements IAbstractConverter<UserDTO,UserEntity>{

    @Autowired
    private RoleRepos roleRepos;

   @Override
    public UserDTO toDto(UserEntity entity) {
        UserDTO dto = new UserDTO();
        dto.setUsername(entity.getUsername());
        dto.setPassword(entity.getPassword());
        dto.setFullName(entity.getFullName());
        dto.setPhone(entity.getPhone());
        dto.setAddress(entity.getAddress());
        entity.getRoleEntities().stream().forEach(e->{
            dto.getRoleId().add(e.getId());
            dto.getRoleName().add(e.getName());
        });
        return toDto(dto,entity);
    }

    @Override
    public UserEntity toEntity(UserDTO dto) {
        UserEntity result = new UserEntity();
        return getUserEntity(result, dto);
    }

    @Override
    public UserEntity toEntity(UserEntity entity, UserDTO dto) {
        return getUserEntity(entity, dto);
    }

    @NotNull
    private UserEntity getUserEntity(UserEntity entity, UserDTO dto) {
        if( dto.getId() != null ) entity.setId(dto.getId());
        entity.setUsername(dto.getUsername());
        entity.setPassword(dto.getPassword());
        entity.setFullName(dto.getFullName());
        entity.setPhone(dto.getPhone());
        entity.setAddress(dto.getAddress());
        dto.getRoleId().forEach(roleId->{entity.getRoleEntities().add(roleRepos.findById(roleId).get());});
        entity.setStatus( dto.getStatus() != null ? dto.getStatus() : true);
        return entity;
    }
}
