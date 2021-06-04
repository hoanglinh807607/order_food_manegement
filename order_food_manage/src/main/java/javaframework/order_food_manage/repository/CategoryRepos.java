package javaframework.order_food_manage.repository;

import javaframework.order_food_manage.entities.CategoryEntity;
import javaframework.order_food_manage.entities.ContactEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.net.ContentHandler;
import java.util.List;

@Repository
public interface CategoryRepos extends PagingAndSortingRepository<CategoryEntity, Long> {
    @Query("select c from CategoryEntity c where concat(c.code, c.name) like %:keyword% and c.status = :status")
    public List<CategoryEntity> findAllSearch(@Param("keyword") String keyword, Pageable pageable, @Param("status") Boolean status);

    @Query("select c from CategoryEntity c where concat(c.code, c.name) like %?1% and c.status = ?2")
    public List<CategoryEntity> findAllSearch(String keyword, Boolean status);

    @Query("select c from CategoryEntity c where c.status = :status")
    public List<CategoryEntity> findAll(Pageable pageable, @Param("status") Boolean status);

    @Query("Select Count(c) from CategoryEntity c where concat(c.code, c.name) like %?1%")
    public int countSearch(String keyword);

    @Query("Select Count(c) from CategoryEntity c where c.status = ?1")
    public int count(Boolean status);

}
