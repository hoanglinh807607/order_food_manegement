package javaframework.order_food_manage.repository;

import javaframework.order_food_manage.entities.FoodEntity;
import javaframework.order_food_manage.entities.FoodGroupEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepos extends PagingAndSortingRepository<FoodEntity, Long> {
    @Query("Select f from FoodEntity f where f.categoryEntity.code = :categoryCode and f.status = :status ")
    public List<FoodEntity> findByCategoryCode( @Param("categoryCode")String categoryCode, Pageable pageable, @Param("status") Boolean status);

    @Query("Select f from FoodEntity f where f.foodGroupEntity.id = ?1")
    public List<FoodEntity> findByFoodGroupId(Long foodGroupId);

    @Query("Select f from FoodEntity f where f.price_promotion > 0")
    public List<FoodEntity> findAllHavePricePromotion();

    @Query("select f from FoodEntity f where concat(f.name, f.description, f.price, f.price_promotion, f.categoryEntity.name, f.foodGroupEntity.name) like %:keyword% and f.status = :status")
    public List<FoodEntity> findAllSearch(@Param("keyword")String keyword, Pageable pageable,@Param("status") Boolean status);

    @Query("select f from FoodEntity f where concat(f.name, f.description, f.price, f.price_promotion, f.categoryEntity.name, f.foodGroupEntity.name) like %?1% and f.status = ?2")
    public List<FoodEntity> findAllSearch(String keyword, Boolean status);

    @Query("select c from FoodEntity c where c.status = :status")
    public List<FoodEntity> findAll(Pageable pageable, @Param("status") Boolean status);

    @Query("Select Count(f) from FoodEntity f where concat(f.name, f.description, f.price, f.price_promotion, f.categoryEntity.name, f.foodGroupEntity.name) like %?1%")
    public int countSearch(String keyword);

    @Query("Select Count(c) from FoodEntity c where c.status = ?1")
    public int count(Boolean status);

    @Query("select distinct f.categoryEntity.id from FoodEntity f")
    public List<Long> getListCategoryIdUnduplicated();

    @Query("select distinct f.foodGroupEntity.id from FoodEntity f")
    public List<Long> getListFoodGroupIdUnduplicated();

}
