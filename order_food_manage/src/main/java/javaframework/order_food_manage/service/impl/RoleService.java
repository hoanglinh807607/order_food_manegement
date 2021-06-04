package javaframework.order_food_manage.service.impl;

import javaframework.order_food_manage.converter.RoleConverter;
import javaframework.order_food_manage.dto.RoleDTO;
import javaframework.order_food_manage.entities.RoleEntity;
import javaframework.order_food_manage.repository.RoleRepos;
import javaframework.order_food_manage.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleService implements IRoleService {

    @Autowired
    private RoleRepos roleRepos;

    @Autowired
    private RoleConverter roleConverter;

    @Override
    public RoleDTO findOne(Long id) {
        return roleConverter.toDto(roleRepos.findById(id).get());
    }

    @Override
    public List<RoleDTO> findAll() {
        List<RoleDTO> roleDTOS = new ArrayList<>();
        List<RoleEntity> roleEntities = (List<RoleEntity>) roleRepos.findAll();
        roleEntities.forEach(roleEntity -> {
            roleDTOS.add(roleConverter.toDto(roleEntity));
        });
        return roleDTOS;
    }

    @Override
    public RoleDTO save(RoleDTO roleDTO) {
        RoleEntity roleEntity = new RoleEntity();
        RoleDTO result = new RoleDTO();
        boolean checkInsert = false;
        try {
            if (roleDTO.getId() != null) {
                RoleEntity entity_old = roleRepos.findById(roleDTO.getId()).get();
                roleEntity = roleConverter.toEntity( entity_old, roleDTO);
            } else {
                roleEntity = roleConverter.toEntity(roleDTO);
                checkInsert = true;
            }
            result = roleConverter.toDto(roleRepos.save(roleEntity));
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if( result.getId() != null ){
                result.setAlert("success");
                if( checkInsert ) {
                    result.setMessage("Insert success");
                }else{
                    result.setMessage("Update success");
                }
            }else{
                result.setAlert("danger");
                result.setMessage("No success");
            }
        }
        return result;
    }

    @Override
    public void delete(Long[] ids) {
        for (Long item : ids) {
            RoleEntity entity = roleRepos.findById(item).get();
            entity.setStatus(false);
            roleRepos.save(entity);
        }
    }

    @Override
    public void permanentDelete(Long[] ids) {
        for (Long item : ids) {
            roleRepos.deleteById(item);
        }
    }

    @Override
    public void restore(Long[] ids) {
        for (Long item : ids) {
            RoleEntity entity = roleRepos.findById(item).get();
            entity.setStatus(true);
            roleRepos.save(entity);
        }
    }

    @Override
    public RoleDTO getAllPagination(int page, int limit, String keyword,Boolean status) {
        RoleDTO roleDTO = new RoleDTO();
        List<RoleEntity> roleEntities = new ArrayList<>();
        int total = 0;
        if (limit == 1) {
            if( !keyword.equals("") ){
                roleEntities = (List<RoleEntity>) roleRepos.findAllSearch(keyword,status);
            }else {
                roleEntities = (List<RoleEntity>) roleRepos.findAll();
            }
        } else {
            if( !keyword.equals("")){
                roleEntities = roleRepos.findAllSearch(keyword, PageRequest.of(page-1, limit), status);
                total = (int) Math.ceil((double) countSearch(keyword) / limit);
            }else {
                roleEntities = roleRepos.findAll(PageRequest.of(page - 1, limit),status);
                total = (int) Math.ceil((double) totalItem(status) / limit);
            }
        }
        roleEntities.forEach(result -> {
            roleDTO.getResultList().add(roleConverter.toDto(result));
        });
        roleDTO.setTotalPage(total);
        roleDTO.setPage(page);
        roleDTO.setLimit(limit);
        roleDTO.setSearch(keyword);
        return roleDTO;
    }

    @Override
    public int totalItem(Boolean status) {
        return (int) roleRepos.count(status);
    }

    @Override
    public int countSearch(String keySearch) {
        return roleRepos.countSearch(keySearch);
    }
}
