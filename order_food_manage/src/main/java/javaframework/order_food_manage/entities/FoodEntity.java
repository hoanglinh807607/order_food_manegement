package javaframework.order_food_manage.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data
@Table(name="food")
public class FoodEntity extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Long price;

    @Column(name = "price_promotion")
    private Long price_promotion;

    @ManyToOne
    @JoinColumn(name = "group_food_id")
    private FoodGroupEntity foodGroupEntity;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity categoryEntity;

    @OneToMany(mappedBy = "foodEntity")
    private Collection<OrderDetailEntity> orderDetailEntities;

    @OneToMany(mappedBy = "foodEntity")
    private Collection<ImageEntity> imageEntities;

}
