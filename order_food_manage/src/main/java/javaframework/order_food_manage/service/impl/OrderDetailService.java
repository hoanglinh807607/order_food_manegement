package javaframework.order_food_manage.service.impl;

import javaframework.order_food_manage.converter.OrderDetailConverter;
import javaframework.order_food_manage.dto.OrderDTO;
import javaframework.order_food_manage.dto.OrderDetailDTO;
import javaframework.order_food_manage.entities.OrderDetailEntity;
import javaframework.order_food_manage.entities.OrderEntity;
import javaframework.order_food_manage.repository.OrderDetailRepos;
import javaframework.order_food_manage.service.IOrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderDetailService implements IOrderDetailService {

    @Autowired
    private OrderDetailConverter orderDetailConverter;

    @Autowired
    private OrderDetailRepos orderDetailRepos;

    @Override
    public OrderDetailDTO save(OrderDetailDTO orderDetailDTO) {
        OrderDetailEntity orderDetailEntity = new OrderDetailEntity();
        OrderDetailDTO result = null;
        boolean checkInsert = false;
        try {
            if (orderDetailDTO.getId() != null) {
                OrderDetailEntity entity_old = orderDetailRepos.findById(orderDetailDTO.getId()).get();
                orderDetailEntity = orderDetailConverter.toEntity( entity_old, orderDetailDTO);
            } else {
                orderDetailEntity = orderDetailConverter.toEntity(orderDetailDTO);
                checkInsert = true;
            }
            result = orderDetailConverter.toDto(orderDetailRepos.save(orderDetailEntity));
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if( result.getId() != null ){
                result.setAlert("success");
                if( checkInsert ) {
                    result.setMessage("Insert success");
                }else{
                    result.setMessage("Update success");
                }
            }else{
                result.setAlert("danger");
                result.setMessage("No success");
            }
        }
        return result;
    }

    @Override
    public void delete(Long[] ids) {
        for (Long item : ids) {
            OrderDetailEntity entity = orderDetailRepos.findById(item).get();
            entity.setStatus(false);
            orderDetailRepos.save(entity);
        }
    }

    @Override
    public void permanentDelete(Long[] ids) {
        for (Long item : ids) {
            orderDetailRepos.deleteById(item);
        }
    }

    @Override
    public void restore(Long[] ids) {
        for (Long item : ids) {
            OrderDetailEntity entity = orderDetailRepos.findById(item).get();
            entity.setStatus(true);
            orderDetailRepos.save(entity);
        }
    }

    @Override
    public OrderDetailDTO findOne(Long id) {
        return orderDetailConverter.toDto(orderDetailRepos.findById(id).get());
    }

    @Override
    public List<OrderDetailDTO> findAll() {
        List<OrderDetailDTO> orderDetailDTOS = new ArrayList<>();
        List<OrderDetailEntity> orderDetailEntities = (List<OrderDetailEntity>) orderDetailRepos.findAll();
        orderDetailEntities.forEach(orderDetailEntity -> {
            orderDetailDTOS.add(orderDetailConverter.toDto(orderDetailEntity));
        });
        return orderDetailDTOS;
    }

    @Override
    public int totalItem(Boolean status) {
        return 0;
    }

    @Override
    public int countSearch(String keySearch) {
        return 0;
    }

    @Override
    public OrderDetailDTO getAllPagination(int page, int limit, String search, Boolean status) {
        return null;
    }

}
