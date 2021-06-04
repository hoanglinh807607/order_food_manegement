package javaframework.order_food_manage.service.impl;

import javaframework.order_food_manage.converter.FoodConverter;
import javaframework.order_food_manage.dto.FoodDTO;
import javaframework.order_food_manage.entities.FoodEntity;
import javaframework.order_food_manage.entities.ImageEntity;
import javaframework.order_food_manage.repository.FoodRepos;
import javaframework.order_food_manage.repository.ImageRepos;
import javaframework.order_food_manage.service.IFoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FoodService implements IFoodService {

    @Autowired
    private FoodRepos foodRepos;

    @Autowired
    private FoodConverter foodConverter;

    @Autowired
    private ImageRepos imageRepos;

    @Override
    public FoodDTO findOne(Long id) {
        return foodConverter.toDto(foodRepos.findById(id).get());
    }

    @Override
    public FoodDTO getFoodByCategoryCodeAndPagination(String categoryCode, int page, int limit, Boolean status) {
        FoodDTO foodDTO = new FoodDTO();
        List<FoodEntity> foodEntities = foodRepos.findByCategoryCode(categoryCode, PageRequest.of(page - 1, limit),status);
        foodEntities.forEach(f->foodDTO.getResultList().add(foodConverter.toDto(f)));
        foodDTO.setTotalPage((int) Math.ceil((double) totalItem(status) / limit));
        foodDTO.setPage(page);
        foodDTO.setLimit(limit);
        return foodDTO;
    }

    @Override
    public FoodDTO filterPrice(FoodDTO foodDTO, int filterPrice) {
        switch (filterPrice){
            case 1: foodDTO.setResultList(foodDTO.getResultList().stream().filter(food->{
                if( food.getPricePromotion() != 0 ) return food.getPricePromotion()<100000;
                else return food.getPrice()<100000;
            }).collect(Collectors.toList())); break;
            case 2:  foodDTO.setResultList(foodDTO.getResultList().stream().filter(food->{
                if( food.getPricePromotion() != 0 ) return food.getPricePromotion()>=100000 && food.getPricePromotion()<200000;
                else return food.getPrice()>=100000 && food.getPrice()<200000;
            }).collect(Collectors.toList())); break;
            case 3:  foodDTO.setResultList(foodDTO.getResultList().stream().filter(food->{
                    if( food.getPricePromotion() != 0 ) return food.getPricePromotion()>=200000 && food.getPricePromotion()<300000;
                else return food.getPrice()>=200000 && food.getPrice()<300000;
            }).collect(Collectors.toList())); break;
            case 4:  foodDTO.setResultList(foodDTO.getResultList().stream().filter(food->{
                if( food.getPricePromotion() != 0 ) return food.getPricePromotion()>=300000 && food.getPricePromotion()<=500000;
                else return food.getPrice()>=300000 && food.getPrice()<=500000;
            }).collect(Collectors.toList())); break;
            case 5:  foodDTO.setResultList(foodDTO.getResultList().stream().filter(food->{
                if( food.getPricePromotion() != 0 ) return food.getPricePromotion()>500000;
                else return food.getPrice()>500000;
            }).collect(Collectors.toList())); break;
        }
        return foodDTO;
    }

    @Override
    public FoodDTO sorter(FoodDTO foodDTO,int idSort) {
        switch (idSort){
            case 1: foodDTO.getResultList().sort((food1,food2)-> {if(food1.getName().compareTo(food2.getName()) < 0) return -1;
                return 0;
            }); break;
            case 2: foodDTO.getResultList().sort((food1,food2)-> {if(food1.getName().compareTo(food2.getName()) > 0) return -1;
                return 0;
            }); break;
            case 3: foodDTO.getResultList().sort((food1,food2)->{return (int) (food1.getPrice()-food2.getPrice());}); break;
            case 4: foodDTO.getResultList().sort((food1,food2)->{return (int) (food2.getPrice()-food1.getPrice());}); break;
            case 5: foodDTO.setResultList(foodDTO.getResultList().stream().filter(food -> {
                return food.getFoodGroupId() == 1;
            }).collect(Collectors.toList())); break;
        }
        return foodDTO;
    }

    @Override
    public List<FoodDTO> findByFoodGroupId(Long foodGroupId) {
        List<FoodDTO> foodDTOS = new ArrayList<>();
        List<FoodEntity> foodEntities = foodRepos.findByFoodGroupId(foodGroupId);
        foodEntities.forEach(f->{
            foodDTOS.add(foodConverter.toDto(f));
        });
        return foodDTOS;
    }

    @Override
    public List<FoodDTO> findAllHavePricePromotion() {
        List<FoodDTO> foodDTOS = new ArrayList<>();
        List<FoodEntity> foodEntities = foodRepos.findAllHavePricePromotion();
        foodEntities.forEach(f->{
            foodDTOS.add(foodConverter.toDto(f));
        });
        return foodDTOS;
    }

    @Override
    public List<FoodDTO> findAll() {
        List<FoodDTO> foodDTOS = new ArrayList<>();
        List<FoodEntity> foodEntities = (List<FoodEntity>) foodRepos.findAll();
        foodEntities.forEach(f->{
            foodDTOS.add(foodConverter.toDto(f));
        });
        return foodDTOS;
    }

    @Override
    public FoodDTO save(FoodDTO foodDTO) {
        FoodEntity foodEntity = new FoodEntity();
        FoodDTO result = new FoodDTO();
        boolean checkInsert = false;
        if (foodDTO.getId() != null) {
            FoodEntity entity_old = foodRepos.findById(foodDTO.getId()).get();
            foodEntity = foodConverter.toEntity(entity_old, foodDTO);
        } else {
            foodEntity = foodConverter.toEntity(foodDTO);
            checkInsert = true;
        }
        try {
            result = foodConverter.toDto(foodRepos.save(foodEntity));
        }catch ( Exception e){
            e.printStackTrace();
        }finally {
            if (result.getId() != null) {
                result.setAlert("success");
                if (checkInsert) {
                    result.setMessage("Insert success");
                } else {
                    result.setMessage("Update success");
                }
            } else {
                result.setAlert("danger");
                result.setMessage("No success");
            }
        }
        return result;
    }

    @Override
    public void delete(Long[] ids) {
        for (Long item : ids) {
            List<ImageEntity> imageEntities =  imageRepos.findByFoodId(item);
            if( imageEntities.size() > 0 ){
                imageEntities.forEach(i->{
                    i.setStatus(false);
                    imageRepos.save(i);
                });
            }
            FoodEntity entity = foodRepos.findById(item).get();
            entity.setStatus(false);
            foodRepos.save(entity);
        }
    }

    @Override
    public void permanentDelete(Long[] ids) {
        for (Long item : ids) {
            foodRepos.deleteById(item);
        }
    }

    @Override
    public void restore(Long[] ids) {
        for (Long item : ids) {
            List<ImageEntity> imageEntities =  imageRepos.findByFoodId(item);
            if( imageEntities.size() > 0 ){
                imageEntities.forEach(i->{
                    i.setStatus(true);
                    imageRepos.save(i);
                });
            }
            FoodEntity entity = foodRepos.findById(item).get();
            entity.setStatus(true);
            foodRepos.save(entity);
        }
    }

    @Override
    public FoodDTO getAllPagination(int page, int limit, String keyword,Boolean status) {
        FoodDTO foodDTO = new FoodDTO();
        List<FoodEntity> foodEntities = new ArrayList<>();
        int total = 0;
        if (limit == 1) {
            if( !keyword.equals("") ){
                foodEntities = (List<FoodEntity>) foodRepos.findAllSearch(keyword,status);
            }else {
                foodEntities = (List<FoodEntity>) foodRepos.findAll();
            }
        } else {
            if( !keyword.equals("")){
                foodEntities = foodRepos.findAllSearch(keyword, PageRequest.of(page-1, limit),status);
                total = (int) Math.ceil((double) countSearch(keyword) / limit);
            }else {
                foodEntities = foodRepos.findAll(PageRequest.of(page - 1, limit),status);
                total = (int) Math.ceil((double) totalItem(status) / limit);
            }
        }
        foodEntities.forEach(result -> {
            foodDTO.getResultList().add(foodConverter.toDto(result));
        });
        foodDTO.setTotalPage(total);
        foodDTO.setPage(page);
        foodDTO.setLimit(limit);
        foodDTO.setSearch(keyword);
        return foodDTO;
    }

    @Override
    public int totalItem(Boolean status) {
        return (int) foodRepos.count(status);
    }

    @Override
    public int countSearch(String keySearch) {
        return foodRepos.countSearch(keySearch);
    }

    @Override
    public List<Long> getListCategoryIdUnduplicated() {
        List<Long> listCategoryId = foodRepos.getListCategoryIdUnduplicated();
        return listCategoryId;
    }

    @Override
    public List<Long> getFoodGroupIdUnduplicated() {
        List<Long> listFoodGroupId = foodRepos.getListFoodGroupIdUnduplicated();
        return listFoodGroupId;
    }
}
