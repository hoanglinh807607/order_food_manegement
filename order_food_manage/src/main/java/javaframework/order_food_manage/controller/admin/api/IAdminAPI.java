package javaframework.order_food_manage.controller.admin.api;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;

public interface IAdminAPI<T> {
    String doAdd(Model model, @ModelAttribute T obj);
    String doEdit(Model model,@ModelAttribute T obj);
    String doDelete(@RequestBody T obj);
}
