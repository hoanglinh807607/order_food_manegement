package javaframework.order_food_manage.service.impl;

import javaframework.order_food_manage.converter.ContactConverter;
import javaframework.order_food_manage.dto.CategoryDTO;
import javaframework.order_food_manage.dto.ContactDTO;
import javaframework.order_food_manage.entities.CategoryEntity;
import javaframework.order_food_manage.entities.ContactEntity;
import javaframework.order_food_manage.repository.ContactRepos;
import javaframework.order_food_manage.service.IContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContactService implements IContactService {

    @Autowired
    private ContactRepos contactRepos;

    @Autowired
    private ContactConverter contactConverter;

    @Override
    public List<ContactDTO> findAll() {
        List<ContactDTO> contactDTOS = new ArrayList<>();
        List<ContactEntity> contactEntities = (List<ContactEntity>) contactRepos.findAll();
        contactEntities.forEach(categoryEntity -> {
            contactDTOS.add(contactConverter.toDto(categoryEntity));
        });
        return contactDTOS;
    }

    @Override
    public ContactDTO save(ContactDTO contactDTO) {
        ContactEntity contactEntity = new ContactEntity();
        ContactDTO contact = new ContactDTO();
        boolean checkInsert = false;
        try {
            if (contactDTO.getId() != null) {
                ContactEntity entity_old = contactRepos.findById(contactDTO.getId()).get();
                contactEntity = contactConverter.toEntity( entity_old, contactDTO);
            } else {
                contactEntity = contactConverter.toEntity(contactDTO);
                checkInsert = true;
            }
            contact = contactConverter.toDto(contactRepos.save(contactEntity));
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if( contact .getId() != null ){
                contact.setAlert("success");
                if( checkInsert ) {
                    contact.setMessage("Insert success");
                }else{
                    contact.setMessage("Update success");
                }
            }else{
                contact.setAlert("danger");
                contact.setMessage("No success");
            }
        }
        return contact;
    }

    @Override
    public void delete(Long[] ids) {
        for (Long item : ids) {
            ContactEntity entity = contactRepos.findById(item).get();
            entity.setStatus(false);
            contactRepos.save(entity);
        }
    }

    @Override
    public void permanentDelete(Long[] ids) {
        for (Long item : ids) {
            contactRepos.deleteById(item);
        }
    }

    @Override
    public void restore(Long[] ids) {
        for (Long item : ids) {
            ContactEntity entity = contactRepos.findById(item).get();
            entity.setStatus(true);
            contactRepos.save(entity);
        }
    }

    @Override
    public ContactDTO getAllPagination(int page, int limit, String keyword,Boolean status) {
        ContactDTO contactDTO = new ContactDTO();
        List<ContactEntity> contactEntities = new ArrayList<>();
        int total = 0;
        if (limit == 1) {
            if( !keyword.equals("") ){
                contactEntities = (List<ContactEntity>) contactRepos.findAllSearch(keyword,status);
            }else{
                contactEntities = (List<ContactEntity>) contactRepos.findAll();
            }

        } else {
            if( !keyword.equals("") ){
                contactEntities = contactRepos.findAllSearch(keyword, PageRequest.of(page-1, limit),status);
                total = (int) Math.ceil((double) countSearch(keyword) / limit);
            }else {
                contactEntities = contactRepos.findAll(PageRequest.of(page - 1, limit),status);
                total = (int) Math.ceil((double) totalItem(status) / limit);
            }
        }
        contactEntities.forEach(result -> {
            contactDTO.getResultList().add(contactConverter.toDto(result));
        });
        contactDTO.setTotalPage(total);
        contactDTO.setPage(page);
        contactDTO.setLimit(limit);
        contactDTO.setSearch(keyword);
        return contactDTO;
    }

    @Override
    public int totalItem(Boolean status) {
        return (int) contactRepos.count(status);
    }

    @Override
    public int countSearch(String keySearch) {
        return contactRepos.countSearch(keySearch);
    }

    @Override
    public ContactDTO findOne(Long id) {
        return contactConverter.toDto(contactRepos.findById(id).get());
    }
}
