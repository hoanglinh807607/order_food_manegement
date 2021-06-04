package javaframework.order_food_manage.repository;

import javaframework.order_food_manage.entities.ImageEntity;
import javaframework.order_food_manage.entities.OrderEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepos extends PagingAndSortingRepository<OrderEntity, Long> {
    @Query("select u from OrderEntity u where concat(u.code, u.total, u.orderStatus, u.userCustomerEntity.username, u.userManagerEntity.username) like %:keyword% and u.status = :status")
    public List<OrderEntity> findAllSearch(@Param("keyword")String keyword, Pageable pageable,@Param("status") Boolean status);

    @Query("select u from OrderEntity u where concat(u.code, u.total, u.orderStatus, u.userCustomerEntity.username, u.userManagerEntity.username) like %?1% and u.status = ?2")
    public List<OrderEntity> findAllSearch(String keyword, Boolean status);

    @Query("select c from OrderEntity c where c.status = :status")
    public List<OrderEntity> findAll(Pageable pageable, @Param("status") Boolean status);

    @Query("Select Count(u) from OrderEntity u where concat(u.code, u.total, u.orderStatus, u.userCustomerEntity.username, u.userManagerEntity.username) like %?1%")
    public int countSearch(String keyword);

    @Query("Select Count(c) from OrderEntity c where c.status = ?1")
    public int count(Boolean status);
}
