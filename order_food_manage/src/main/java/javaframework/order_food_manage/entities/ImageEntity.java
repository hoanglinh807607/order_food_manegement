package javaframework.order_food_manage.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "image")
public class ImageEntity extends BaseEntity{

    @Column(name = "path")
    private String path;

    @Column(name = "is_preview")
    private Boolean isPreview;

    @ManyToOne
    @JoinColumn(name = "food_id")
    private FoodEntity foodEntity;

}
