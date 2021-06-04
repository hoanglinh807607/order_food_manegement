package javaframework.order_food_manage.service.impl;

import javaframework.order_food_manage.converter.CategoryConverter;
import javaframework.order_food_manage.dto.CategoryDTO;
import javaframework.order_food_manage.entities.CategoryEntity;
import javaframework.order_food_manage.repository.CategoryRepos;
import javaframework.order_food_manage.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService implements ICategoryService {

    @Autowired
    private CategoryRepos categoryRepos;

    @Autowired
    private CategoryConverter categoryConverter;

    @Autowired
    private FoodService foodService;

    @Override
    public List<CategoryDTO> findAll() {
        List<CategoryDTO> categoryDTOS = new ArrayList<>();
        List<CategoryEntity> categoryEntities = (List<CategoryEntity>) categoryRepos.findAll();
        categoryEntities.forEach(categoryEntity -> {
            categoryDTOS.add(categoryConverter.toDto(categoryEntity));
        });
        return categoryDTOS;
    }

    @Override
    public CategoryDTO getAllPagination(int page, int limit, String keyword,Boolean status) {
        CategoryDTO categoryDTO = new CategoryDTO();
        List<CategoryEntity> categoryEntities = new ArrayList<>();
        List<CategoryDTO> categoryDTOS = new ArrayList<>();
        int total = 0;
        if (limit == 1) {
            if( !keyword.equals("") ){
                categoryEntities = (List<CategoryEntity>) categoryRepos.findAllSearch(keyword,status);
            }else{
                categoryEntities = (List<CategoryEntity>) categoryRepos.findAll();
            }
        } else {
            if( !keyword.equals("") ){
                categoryEntities = categoryRepos.findAllSearch(keyword, PageRequest.of(page-1, limit),status);
                total = (int) Math.ceil((double) countSearch(keyword) / limit);
            }else {
                categoryEntities = categoryRepos.findAll(PageRequest.of(page - 1, limit),status);
                total = (int) Math.ceil((double) totalItem(status) / limit);
            }
        }
        categoryEntities.forEach(result -> {
            categoryDTOS.add(categoryConverter.toDto(result));
        });
        categoryDTO.setTotalPage(total);
        categoryDTO.setResultList(categoryDTOS);
        categoryDTO.setPage(page);
        categoryDTO.setLimit(limit);
        categoryDTO.setSearch(keyword);
        return categoryDTO;
    }

    @Override
    public int totalItem(Boolean status) {
        return (int) categoryRepos.count(status);
    }

    @Override
    public int countSearch(String keySearch) {
        return categoryRepos.countSearch(keySearch);
    }

    @Override
    public CategoryDTO findOne(Long id) {
        return categoryConverter.toDto(categoryRepos.findById(id).get());
    }

    @Override
    public CategoryDTO save(CategoryDTO categoryDTO) {
        CategoryEntity categoryEntity = new CategoryEntity();
        CategoryDTO category = new CategoryDTO();
        boolean checkInsert = false;
        if (categoryDTO.getId() != null) {
            CategoryEntity entity_old = categoryRepos.findById(categoryDTO.getId()).get();
            categoryEntity = categoryConverter.toEntity( entity_old, categoryDTO);
        } else {
            categoryEntity = categoryConverter.toEntity(categoryDTO);
            checkInsert = true;
        }
        category = categoryConverter.toDto((CategoryEntity) categoryRepos.save(categoryEntity));
        if( category != null ){
            category.setAlert("success");
            if( checkInsert ) {
                category.setMessage("Insert success");
            }else{
                category.setMessage("Update success");
            }
        }else{
            category.setAlert("danger");
            category.setMessage("No success");
        }
        return category;
    }

    @Override
    public void delete(Long[] ids) {
        for (Long item : ids) {
            CategoryEntity entity = categoryRepos.findById(item).get();
            entity.setStatus(false);
            categoryRepos.save(entity);
        }
    }

    @Override
    public void permanentDelete(Long[] ids) {
        for (Long item : ids) {
            categoryRepos.deleteById(item);
        }
    }

    @Override
    public void restore(Long[] ids) {
        for (Long item : ids) {
            CategoryEntity entity = categoryRepos.findById(item).get();
            entity.setStatus(true);
            categoryRepos.save(entity);
        }
    }
}
