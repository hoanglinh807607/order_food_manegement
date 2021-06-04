package javaframework.order_food_manage.converter;

import javaframework.order_food_manage.dto.CategoryDTO;
import javaframework.order_food_manage.entities.CategoryEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class CategoryConverter extends AbstractConverter<CategoryDTO> implements IAbstractConverter<CategoryDTO,CategoryEntity>{

    @Override
    public CategoryDTO toDto(CategoryEntity entity) {
        CategoryDTO dto = new CategoryDTO();
        dto.setCode(entity.getCode());
        dto.setName(entity.getName());
        return toDto(dto,entity);
    }

    @Override
    public CategoryEntity toEntity(CategoryDTO dto) {
        CategoryEntity entity = new CategoryEntity();
        return getCategoryEntity(entity,dto);
    }

    @Override
    public CategoryEntity toEntity(CategoryEntity entity, CategoryDTO dto) {
        return getCategoryEntity(entity,dto);
    }

    @NotNull
    private CategoryEntity getCategoryEntity(CategoryEntity entity, CategoryDTO dto) {
        entity.setCode(dto.getCode());
        entity.setName(dto.getName());
        entity.setStatus( dto.getStatus() != null ? dto.getStatus() : true);
        return entity;
    }
}
