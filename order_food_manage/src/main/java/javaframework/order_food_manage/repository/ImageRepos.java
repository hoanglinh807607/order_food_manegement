package javaframework.order_food_manage.repository;

import javaframework.order_food_manage.entities.FoodEntity;
import javaframework.order_food_manage.entities.ImageEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepos extends PagingAndSortingRepository<ImageEntity, Long> {
    @Query("select f from ImageEntity f where concat(f.foodEntity.name, f.isPreview) like %:keyword% and f.status = :status")
    public List<ImageEntity> findAllSearch(@Param("keyword")String keyword, Pageable pageable,@Param("status") Boolean status);

    @Query("select f from ImageEntity f where concat(f.foodEntity.name, f.isPreview) like %?1% and f.status = ?2")
    public List<ImageEntity> findAllSearch(String keyword, Boolean status);

    @Query("select c from ImageEntity c where c.status = :status")
    public List<ImageEntity> findAll(Pageable pageable, @Param("status") Boolean status);

    @Query("Select Count(f) from ImageEntity f where concat(f.foodEntity.name, f.isPreview) like %?1%")
    public int countSearch(String keyword);

    @Query("Select Count(c) from ImageEntity c where c.status = ?1")
    public int count(Boolean status);

    @Query("Select f from ImageEntity f where f.foodEntity.id = ?1")
    public List<ImageEntity> findByFoodId(Long id);

}
