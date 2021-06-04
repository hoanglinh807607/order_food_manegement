package javaframework.order_food_manage.service.impl;

import javaframework.order_food_manage.constant.SystemConstant;
import javaframework.order_food_manage.converter.OrderConverter;
import javaframework.order_food_manage.dto.OrderDTO;
import javaframework.order_food_manage.dto.OrderDetailDTO;
import javaframework.order_food_manage.entities.OrderEntity;
import javaframework.order_food_manage.repository.OrderDetailRepos;
import javaframework.order_food_manage.repository.OrderRepos;
import javaframework.order_food_manage.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService implements IOrderService {

    @Autowired
    private OrderRepos orderRepos;

    @Autowired
    private OrderConverter orderConverter;

    @Autowired
    private OrderDetailService orderDetailService;

    @Override
    public OrderDTO getOrderDto(HttpSession session) {
        OrderDTO order = (OrderDTO) session.getAttribute(SystemConstant.SESSION_KEY_CART);
        if( order == null ){
            order = new OrderDTO();
            setOrderDto(session,order);
        }
        return order;
    }

    @Override
    public void setOrderDto(HttpSession session, OrderDTO orderDTO) {
        session.setAttribute(SystemConstant.SESSION_KEY_CART,orderDTO);
        setTotalQuantity(session,orderDTO);
    }

    @Override
    public void removeOrderDTO(HttpSession session) {
        session.removeAttribute(SystemConstant.SESSION_KEY_CART);
    }

    @Override
    public void setTotalQuantity(HttpSession session, OrderDTO orderDTO) {
        Integer quantity = 0;
        for (OrderDetailDTO orderDetailDTO : orderDTO.getListOrderDetail()) {
            quantity += orderDetailDTO.getQuantity();
        }
        session.setAttribute(SystemConstant.SESSION_TOTAL_QUANTITY,quantity);
    }

    @Override
    public OrderDTO save(OrderDTO orderDTO) {
        OrderEntity orderEntity = new OrderEntity();
        OrderDTO result = new OrderDTO();
        boolean checkInsert = false;
        try {
            if (orderDTO.getId() != null) {
                OrderEntity entity_old = orderRepos.findById(orderDTO.getId()).get();
                orderEntity = orderConverter.toEntity( entity_old, orderDTO);
            } else {
                orderDTO.setCode(getCodeNext());
                orderDTO.setOrderStatus(0);
                orderEntity = orderConverter.toEntity(orderDTO);
                checkInsert = true;
            }
            result = orderConverter.toDto(orderRepos.save(orderEntity));
            // Nếu nhưng đang thêm đơn hàng thì chúng ta thêm các chi tiết trên đơn hàng đó
            if( checkInsert ) {
                OrderDTO finalResult = result;
                orderDTO.getListOrderDetail().forEach(orderDetailDTO -> {
                    orderDetailDTO.setOrderId(finalResult.getId());
                    orderDetailService.save(orderDetailDTO);
                });
            }
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
            OrderEntity entity = orderRepos.findById(item).get();
            entity.setStatus(false);
            orderRepos.save(entity);
        }
    }

    @Override
    public void permanentDelete(Long[] ids) {
        for (Long item : ids) {
            OrderEntity orderEntity = orderRepos.findById(item).get();
            Long[] arrayLongIds = new Long[orderEntity.getOrderDetailEntities().size()];
            for( int i = 0; i<orderEntity.getOrderDetailEntities().size() ; i++){
                arrayLongIds[i] = orderEntity.getOrderDetailEntities().get(i).getId();
            }
            orderDetailService.permanentDelete(arrayLongIds);
            orderRepos.deleteById(item);
        }
    }

    @Override
    public void restore(Long[] ids) {
        for (Long item : ids) {
            OrderEntity entity = orderRepos.findById(item).get();
            entity.setStatus(true);
            orderRepos.save(entity);
        }
    }

    @Override
    public List<OrderDTO> findAll() {
        List<OrderDTO> orderDTOS = new ArrayList<>();
        List<OrderEntity> orderEntity = (List<OrderEntity>) orderRepos.findAll();
        orderEntity.forEach(entity->{
            orderDTOS.add(orderConverter.toDto(entity));
        });
        return orderDTOS;
    }

    @Override
    public OrderDTO findOne(Long id) {
        return orderConverter.toDto(orderRepos.findById(id).get());
    }

    @Override
    public OrderDTO getAllPagination(int page, int limit, String keyword,Boolean status) {
        OrderDTO dto = new OrderDTO();
        List<OrderEntity> orderEntities = new ArrayList<>();
        int total = 0;
        if (limit == 1) {
            if( !keyword.equals("") ){
                orderEntities = (List<OrderEntity>) orderRepos.findAllSearch(keyword,status);
            }else {
                orderEntities = (List<OrderEntity>) orderRepos.findAll();
            }
        } else {
            if( !keyword.equals("")){
                orderEntities = orderRepos.findAllSearch(keyword, PageRequest.of(page-1, limit),status);
                total = (int) Math.ceil((double) countSearch(keyword) / limit);
            }else {
                orderEntities = orderRepos.findAll(PageRequest.of(page - 1, limit),status);
                total = (int) Math.ceil((double) totalItem(status) / limit);
            }
        }
        orderEntities.forEach(result -> {
            dto.getResultList().add(orderConverter.toDto(result));
        });
        dto.setTotalPage(total);
        dto.setPage(page);
        dto.setLimit(limit);
        dto.setSearch(keyword);
        return dto;
    }

    @Override
    public int totalItem(Boolean status) {
        return (int) orderRepos.count(status);
    }

    @Override
    public int countSearch(String keySearch) {
        return orderRepos.countSearch(keySearch);
    }

    public String getCodeNext() {
        List<OrderEntity> entities = (List<OrderEntity>) orderRepos.findAll();
        if( !entities.isEmpty() ) {
            String strNumberInCode = entities.get(entities.size()-1).getCode().substring(2);
            String ma = new String("HD");
            int chiSo = Integer.parseInt(strNumberInCode)+1;
            String strChiSo = String.valueOf(chiSo);
            for( int i = 0; i <strNumberInCode.length()-strChiSo.length() ; i++ ) {
                ma+='0';
            }
            return ma+chiSo;
        }
        return "HD0001";
    }

}
