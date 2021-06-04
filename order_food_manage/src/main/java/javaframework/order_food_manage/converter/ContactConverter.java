package javaframework.order_food_manage.converter;

import javaframework.order_food_manage.dto.ContactDTO;
import javaframework.order_food_manage.entities.ContactEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class ContactConverter extends AbstractConverter<ContactDTO> implements IAbstractConverter<ContactDTO, ContactEntity>{
    @Override
    public ContactDTO toDto(ContactEntity entity) {
        ContactDTO dto = new ContactDTO();
        dto.setEmail(entity.getEmail());
        dto.setFullName(entity.getFullname());
        dto.setPhone(entity.getPhone());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        dto.setState(entity.getState());
        return toDto(dto,entity);
    }

    @Override
    public ContactEntity toEntity(ContactDTO dto) {
        ContactEntity entity = new ContactEntity();
        return getContactEntity(entity,dto);
    }

    @Override
    public ContactEntity toEntity(ContactEntity entity, ContactDTO dto) {
        return getContactEntity(entity,dto);
    }

    @NotNull
    private ContactEntity getContactEntity(ContactEntity entity, ContactDTO dto) {
        entity.setEmail(dto.getEmail());
        entity.setFullname(dto.getFullName());
        entity.setPhone(dto.getPhone());
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        entity.setState( dto.getState() != null ? dto.getState() : 0);
        entity.setStatus( dto.getStatus() != null ? dto.getStatus() : true);
        return entity;
    }
}
