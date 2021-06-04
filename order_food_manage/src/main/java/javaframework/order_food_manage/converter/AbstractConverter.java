package javaframework.order_food_manage.converter;

import javaframework.order_food_manage.dto.AbstractDTO;
import javaframework.order_food_manage.entities.BaseEntity;
import org.springframework.stereotype.Component;

@Component
public class AbstractConverter<T extends AbstractDTO> {
    public T toDto(T abstractDTO, BaseEntity entity) {
        abstractDTO.setCreatedDate(entity.getCreatedDate() != null ? entity.getCreatedDate().toString() : "");
        abstractDTO.setCreatedBy(entity.getCreatedBy());
        abstractDTO.setModifiedDate(entity.getCreatedDate() != null ? entity.getModifiedDate().toString() : "");
        abstractDTO.setModifiedBy(entity.getModifiedBy());
        abstractDTO.setStatus(entity.getStatus());
        abstractDTO.setId(entity.getId());
        return abstractDTO;

    }


}