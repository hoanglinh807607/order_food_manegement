package javaframework.order_food_manage.repository;

import javaframework.order_food_manage.entities.CategoryEntity;
import javaframework.order_food_manage.entities.ContactEntity;
import javaframework.order_food_manage.entities.FoodGroupEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepos extends PagingAndSortingRepository<ContactEntity, Long> {
    @Query("select c from ContactEntity c where concat(c.id, c.email, c.fullname, c.phone, c.title, c.content) like %:keyword% and c.status = :status")
    public List<ContactEntity> findAllSearch(@Param("keyword")String keyword, Pageable pageable, @Param("status") Boolean status);

    @Query("select c from ContactEntity c where concat(c.id, c.email, c.fullname, c.phone, c.title, c.content) like %?1% and c.status = ?2")
    public List<ContactEntity> findAllSearch(String keyword, Boolean status);

    @Query("select c from ContactEntity c where c.status = :status")
    public List<ContactEntity> findAll(Pageable pageable, @Param("status") Boolean status);

    @Query("Select Count(c) from ContactEntity c where concat(c.id, c.email, c.fullname, c.phone, c.title, c.content) like %?1%")
    public int countSearch(String keyword);

    @Query("Select Count(c) from ContactEntity c where c.status = ?1")
    public int count(Boolean status);
}
