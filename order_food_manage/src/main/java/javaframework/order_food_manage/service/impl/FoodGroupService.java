package javaframework.order_food_manage.service.impl;

import javaframework.order_food_manage.converter.FoodGroupConverter;
import javaframework.order_food_manage.dto.FoodGroupDTO;
import javaframework.order_food_manage.entities.FoodGroupEntity;
import javaframework.order_food_manage.repository.FoodGroupRepos;
import javaframework.order_food_manage.service.IFoodGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FoodGroupService implements IFoodGroupService {
    @Autowired
    private FoodGroupRepos foodGroupRepos;

    @Autowired
    private FoodGroupConverter foodGroupConverter;

    @Autowired
    private FoodService foodService;

    @Override
    public List<FoodGroupDTO> findAll() {
        List<FoodGroupDTO> foodGroupDTOS = new ArrayList<>();
        List<FoodGroupEntity> categoryEntities = (List<FoodGroupEntity>) foodGroupRepos.findAll();
        categoryEntities.forEach(categoryEntity -> {
            foodGroupDTOS.add(foodGroupConverter.toDto(categoryEntity));
        });
        return foodGroupDTOS;
    }

    @Override
    public FoodGroupDTO getAllPagination(int page, int limit, String keyword,Boolean status) {
        FoodGroupDTO foodGroupDTO = new FoodGroupDTO();
        List<FoodGroupEntity> foodGroupEntities = new ArrayList<>();
        int total = 0;
        if (limit == 1) {
            if( !keyword.equals("") ){
                foodGroupEntities = (List<FoodGroupEntity>) foodGroupRepos.findAllSearch(keyword,status);
            }else{
                foodGroupEntities = (List<FoodGroupEntity>) foodGroupRepos.findAll();
            }
        } else {
            if( !keyword.equals("") ){
                foodGroupEntities = foodGroupRepos.findAllSearch(keyword, PageRequest.of(page-1, limit),status);
                total = (int) Math.ceil((double) countSearch(keyword) / limit);
            }else {
                foodGroupEntities = foodGroupRepos.findAll(PageRequest.of(page - 1, limit),status);
                total = (int) Math.ceil((double) totalItem(status) / limit);
            }
        }
        foodGroupEntities.forEach(result -> {
            foodGroupDTO.getResultList().add(foodGroupConverter.toDto(result));
        });
        foodGroupDTO.setTotalPage(total);
        foodGroupDTO.setPage(page);
        foodGroupDTO.setLimit(limit);
        foodGroupDTO.setSearch(keyword);
        return foodGroupDTO;
    }

    @Override
    public int totalItem(Boolean status) {
        return (int) foodGroupRepos.count(status);
    }

    @Override
    public int countSearch(String keySearch) {
        return foodGroupRepos.countSearch(keySearch);
    }

    @Override
    public FoodGroupDTO findOne(Long id) {
        return foodGroupConverter.toDto(foodGroupRepos.findById(id).get());
    }

    @Override
    public FoodGroupDTO save(FoodGroupDTO foodGroupDTO) {
        FoodGroupEntity foodGroupEntity = new FoodGroupEntity();
        FoodGroupDTO foodGroupResult = new FoodGroupDTO();
        boolean checkInsert = false;
        if (foodGroupDTO.getId() != null) {
            FoodGroupEntity entity_old = foodGroupRepos.findById(foodGroupDTO.getId()).get();
            foodGroupEntity = foodGroupConverter.toEntity( entity_old, foodGroupDTO);
        } else {
            foodGroupEntity = foodGroupConverter.toEntity(foodGroupDTO);
            checkInsert = true;
        }
        foodGroupResult = foodGroupConverter.toDto((FoodGroupEntity) foodGroupRepos.save(foodGroupEntity));
        if( foodGroupResult != null ){
            foodGroupResult.setAlert("success");
            if( checkInsert ) {
                foodGroupResult.setMessage("Insert success");
            }else{
                foodGroupResult.setMessage("Update success");
            }
        }else{
            foodGroupResult.setAlert("danger");
            foodGroupResult.setMessage("No success");
        }
        return foodGroupResult;
    }

    @Override
    public void delete(Long[] ids) {
        for (Long item : ids) {
            FoodGroupEntity entity = foodGroupRepos.findById(item).get();
            entity.setStatus(false);
            foodGroupRepos.save(entity);
        }
    }

    @Override
    public void permanentDelete(Long[] ids) {
        for (Long item : ids) {
            foodGroupRepos.deleteById(item);
        }
    }

    @Override
    public void restore(Long[] ids) {
        for (Long item : ids) {
            FoodGroupEntity entity = foodGroupRepos.findById(item).get();
            entity.setStatus(true);
            foodGroupRepos.save(entity);
        }
    }
}
