package javaframework.order_food_manage.service.impl;

import javaframework.order_food_manage.converter.UserConverter;
import javaframework.order_food_manage.dto.UserDTO;
import javaframework.order_food_manage.entities.RoleEntity;
import javaframework.order_food_manage.entities.UserEntity;
import javaframework.order_food_manage.repository.RoleRepos;
import javaframework.order_food_manage.repository.UserRepos;
import javaframework.order_food_manage.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepos userRepos;

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private RoleRepos roleRepos;

    @Override
    public UserDTO findOne(Long id) {
        return userConverter.toDto(userRepos.findById(id).get());
    }

    @Override
    public UserDTO findByUserName(String username) {
        return userConverter.toDto(userRepos.findByUsername(username).get());
    }

    @Override
    public UserDTO save(UserDTO userDTO) {
        UserEntity userEntity = new UserEntity();
        UserDTO result = new UserDTO();
        boolean checkInsert = false;
        try {
            if (userDTO.getId() != null) {
                UserEntity entity_old = userRepos.findById(userDTO.getId()).get();
                userEntity = userConverter.toEntity(entity_old, userDTO);
                if( userEntity.getPassword().length() < 50 ){
                    userEntity.setPassword(new BCryptPasswordEncoder().encode(userEntity.getPassword()));
                }
            } else {
                userEntity = userConverter.toEntity(userDTO);
                userEntity.setPassword(new BCryptPasswordEncoder().encode(userEntity.getPassword()));
                checkInsert = true;
            }
            List<RoleEntity> roles = new ArrayList<>();
            userDTO.getRoleId().forEach(idRole->{ roles.add(roleRepos.findById(idRole).get()); });
            userEntity.setRoleEntities(roles);
            if( !userEntity.getRoleEntities().isEmpty() ){
                userEntity = userRepos.save(userEntity);
            }
            result = userConverter.toDto(userEntity);
        }catch ( Exception e){
            e.printStackTrace();
        }finally {
            if (result.getId() != null ) {
                result.setAlert("success");
                if (checkInsert) {
                    result.setMessage("Insert success");
                } else {
                    result.setMessage("Update success");
                }
            } else {
                result.setAlert("danger");
                result.setMessage("role not null");
            }
        }
        return result;
    }

    @Override
    public void delete(Long[] ids) {
        for (Long item : ids) {
            UserEntity entity = userRepos.findById(item).get();
            entity.setStatus(false);
            userRepos.save(entity);
        }
    }

    @Override
    public void permanentDelete(Long[] ids) {
        for (Long item : ids) {
            userRepos.deleteById(item);
        }
    }

    @Override
    public void restore(Long[] ids) {
        for (Long item : ids) {
            UserEntity entity = userRepos.findById(item).get();
            entity.setStatus(true);
            userRepos.save(entity);
        }
    }

    @Override
    public List<UserDTO> findAll() {
        return null;
    }
//    @Override
//    public void delete(Long[] ids) {
//        for (Long item : ids) {
//            userRepos.deleteById(item);
//        }
//    }

    @Override
    public UserDTO getAllPagination(int page, int limit, String search,Boolean status) {
        UserDTO userDTO = new UserDTO();
        List<UserEntity> userEntities = new ArrayList<>();
        int total = 0;
        if (limit == 1) {
            if( !search.equals("") ){
                userEntities = (List<UserEntity>) userRepos.findAllSearch(search,status);
            }else {
                userEntities = (List<UserEntity>) userRepos.findAll();
            }
        } else {
            if( !search.equals("") ){
                userEntities = userRepos.findAllSearch(search, PageRequest.of(page-1, limit),status);
                total = (int) Math.ceil((double) countSearch(search) / limit);
            }else {
                userEntities = userRepos.findAll(PageRequest.of(page - 1, limit),status);
                total = (int) Math.ceil((double) totalItem(status) / limit);
            }
        }
        userEntities.forEach(result -> userDTO.getResultList().add(userConverter.toDto(result)));
        userDTO.setTotalPage(total);
        userDTO.setPage(page);
        userDTO.setLimit(limit);
        userDTO.setSearch(search);
        return userDTO;
    }

    @Override
    public int totalItem(Boolean status) {
        return (int) userRepos.count(status);
    }

    @Override
    public int countSearch(String keySearch) {
        return userRepos.countSearch(keySearch);
    }

    @Override
    public UserDTO findByUsernameAndStatus(String username, boolean status) {
        UserEntity userEntity = userRepos.findByUsernameAndStatus(username, status);
        if( userEntity != null ){
            return userConverter.toDto(userEntity);
        }
        return null;
    }
}
