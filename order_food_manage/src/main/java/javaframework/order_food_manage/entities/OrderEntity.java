package javaframework.order_food_manage.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@Table(name = "order_food")
public class OrderEntity extends BaseEntity {

    @Column(name = "code")
    private String code;

    @Column(name = "total")
    private Long total;

    @Column(name = "order_status")
    private Integer orderStatus;

    @Column(name = "address")
    private String address;

    @Column(name = "payment_methods")
    private Integer paymentMethods;

    @OneToMany(mappedBy = "orderEntity")
    private List<OrderDetailEntity> orderDetailEntities;

    @ManyToOne
    @JoinColumn(name = "user_manager_id")
    private UserEntity userManagerEntity;

    @ManyToOne
    @JoinColumn(name = "user_customer_id")
    private UserEntity userCustomerEntity;

}
