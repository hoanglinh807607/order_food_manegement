package javaframework.order_food_manage.dto;

import javaframework.order_food_manage.paging.PageResult;
import lombok.Data;

import java.util.Date;

@Data
public class AbstractDTO<T> extends PageResult<T> {
    private Long id;
    private Boolean status;
    private String createdBy;
    private String createdDate;
    private String modifiedBy;
    private String modifiedDate;

    // alert
    private String alert;
    private String message;

    // Delete
    private Long[] ids;

    // Keep value search when searching
    private String search;

    private String table;
}
