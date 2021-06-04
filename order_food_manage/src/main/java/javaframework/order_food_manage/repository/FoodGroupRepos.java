package javaframework.order_food_manage.repository;

import javaframework.order_food_manage.entities.FoodGroupEntity;
import javaframework.order_food_manage.entities.OrderEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodGroupRepos extends PagingAndSortingRepository<FoodGroupEntity, Long> {
    @Query("select c from FoodGroupEntity c where concat(c.code, c.name) like %:keyword% and c.status = :status")
    public List<FoodGroupEntity> findAllSearch(@Param("keyword")String keyword, Pageable pageable, @Param("status") Boolean status);

    @Query("select c from FoodGroupEntity c where concat(c.code, c.name) like %?1% and c.status = ?2")
    public List<FoodGroupEntity> findAllSearch(String keyword, Boolean status);

    @Query("select c from FoodGroupEntity c where c.status = :status")
    public List<FoodGroupEntity> findAll(Pageable pageable, @Param("status") Boolean status);

    @Query("Select Count(u) from FoodGroupEntity u where concat(u.code, u.name) like %?1%")
    public int countSearch(String keyword);

    @Query("Select Count(c) from FoodGroupEntity c where c.status = ?1")
    public int count(Boolean status);
}
