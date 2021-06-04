package javaframework.order_food_manage.repository;

import javaframework.order_food_manage.entities.OrderEntity;
import javaframework.order_food_manage.entities.RoleEntity;
import javaframework.order_food_manage.entities.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepos extends PagingAndSortingRepository<RoleEntity, Long> {
    @Query("select u from RoleEntity u where concat(u.code, u.name) like %:keyword% and u.status = :status")
    public List<RoleEntity> findAllSearch(@Param("keyword")String keyword, Pageable pageable, @Param("status") Boolean status);

    @Query("select u from RoleEntity u where concat(u.code, u.name) like %?1% and u.status = ?2")
    public List<RoleEntity> findAllSearch(String keyword, Boolean status);

    @Query("select c from RoleEntity c where c.status = :status")
    public List<RoleEntity> findAll(Pageable pageable, @Param("status") Boolean status);

    @Query("Select Count(u) from RoleEntity u where concat(u.code, u.name) like %?1%")
    public int countSearch(String keyword);

    @Query("Select Count(c) from RoleEntity c where c.status = ?1")
    public int count(Boolean status);
}

