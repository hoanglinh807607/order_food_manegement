package javaframework.order_food_manage.service;


import javaframework.order_food_manage.dto.FoodDTO;

import java.util.List;

public interface IFoodService extends GenericService<FoodDTO> {
    FoodDTO getFoodByCategoryCodeAndPagination(String categoryCode, int page, int limit, Boolean status);
    FoodDTO filterPrice(FoodDTO foodDTO, int filterPrice);
    FoodDTO sorter(FoodDTO foodDTO,int idSort);
    List<FoodDTO> findByFoodGroupId(Long foodGroupId);
    List<FoodDTO> findAllHavePricePromotion();
    List<Long> getListCategoryIdUnduplicated();
    List<Long> getFoodGroupIdUnduplicated();

}
