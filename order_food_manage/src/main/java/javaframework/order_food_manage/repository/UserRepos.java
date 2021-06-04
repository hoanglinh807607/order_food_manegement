package javaframework.order_food_manage.repository;

import javaframework.order_food_manage.entities.RoleEntity;
import javaframework.order_food_manage.entities.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepos extends PagingAndSortingRepository<UserEntity, Long> {
    @Query("select u from UserEntity u where concat(u.username, u.password, u.fullName, u.address, u.phone) like %:keyword% and u.status = :status")
    public List<UserEntity> findAllSearch(@Param("keyword")String keyword, Pageable pageable, @Param("status") Boolean status);

    @Query("select u from UserEntity u where concat(u.username, u.password, u.fullName, u.address, u.phone) like %?1% and u.status = ?2")
    public List<UserEntity> findAllSearch(String keyword, Boolean status);

    @Query("select c from UserEntity c where c.status = :status")
    public List<UserEntity> findAll(Pageable pageable, @Param("status") Boolean status);

    @Query("Select Count(u) from UserEntity u where concat(u.username, u.password, u.fullName, u.address, u.phone) like %?1%")
    public int countSearch(String keyword);

    @Query("Select Count(c) from UserEntity c where c.status = ?1")
    public int count(Boolean status);

    @Query("Select u from UserEntity u where u.username = ?1")
    public Optional<UserEntity> findByUsername(String username);

    @Query("Select u from UserEntity u where u.username = ?1 and u.status = ?2")
    public UserEntity findByUsernameAndStatus(String userName, boolean status);
}
