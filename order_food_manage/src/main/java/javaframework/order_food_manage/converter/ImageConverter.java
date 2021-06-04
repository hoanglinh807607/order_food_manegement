package javaframework.order_food_manage.converter;

import javaframework.order_food_manage.dto.ImageDTO;
import javaframework.order_food_manage.entities.ImageEntity;
import javaframework.order_food_manage.repository.FoodRepos;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;

@Component
public class ImageConverter extends AbstractConverter<ImageDTO> implements IAbstractConverter<ImageDTO, ImageEntity> {

    @Autowired
    private FoodRepos foodRepos;

    @Override
    public ImageDTO toDto(ImageEntity entity) {
        ImageDTO dto = new ImageDTO();
        dto.setPath(entity.getPath());
        dto.setIs_preview(entity.getIsPreview());
        dto.setFoodId(entity.getFoodEntity().getId());
        dto.setFoodName(entity.getFoodEntity().getName());
        return toDto(dto,entity);
    }

    @Override
    public ImageEntity toEntity(ImageDTO dto) {
        ImageEntity entity = new ImageEntity();
        return getImageEntity(entity,dto);
    }

    @Override
    public ImageEntity toEntity(ImageEntity entity, ImageDTO dto) {
        return getImageEntity(entity,dto);
    }

    @NotNull
    private ImageEntity getImageEntity(ImageEntity entity, ImageDTO dto) {
        if( dto.getPath() != null ) entity.setPath(dto.getPath());
        entity.setIsPreview(dto.getIs_preview());
        entity.setFoodEntity(foodRepos.findById(dto.getFoodId()).get());
        entity.setStatus( dto.getStatus() != null ? dto.getStatus() : true);
        return entity;
    }
}
