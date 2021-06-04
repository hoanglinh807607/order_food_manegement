package javaframework.order_food_manage.controller.admin;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface IAdminController<T>{
    String showList(Model model, HttpSession session,
                       @RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "5") int limit,
                       @RequestParam(defaultValue = "") String search);
    String showEditOrAddPage(Model model, @PathVariable(name = "id", required = false) Long id);
    String showDetail(Model model, @PathVariable(name = "id", required = false) Long id);
}
