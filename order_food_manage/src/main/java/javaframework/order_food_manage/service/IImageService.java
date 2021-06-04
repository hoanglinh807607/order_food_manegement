package javaframework.order_food_manage.service;


import javaframework.order_food_manage.dto.ImageDTO;

import java.util.List;

public interface IImageService extends GenericService<ImageDTO>{
    List<ImageDTO> findByFoodId(Long id);
}
