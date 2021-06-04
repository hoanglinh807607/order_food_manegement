package javaframework.order_food_manage.service;

import javaframework.order_food_manage.dto.CategoryDTO;

import java.util.List;

public interface GenericService<T> {
    T save(T dto);
    void delete(Long[] ids);
    void permanentDelete(Long[] ids);
    void restore(Long[] ids);

    List<T> findAll();
    T findOne(Long id);

    int totalItem(Boolean status);
    int countSearch(String keySearch);
    T getAllPagination(int page, int limit, String search,Boolean status);
}
