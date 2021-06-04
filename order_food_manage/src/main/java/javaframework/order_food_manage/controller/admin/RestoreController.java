package javaframework.order_food_manage.controller.admin;

import javaframework.order_food_manage.constant.SystemConstant;
import javaframework.order_food_manage.dto.AbstractDTO;
import javaframework.order_food_manage.dto.ContactDTO;
import javaframework.order_food_manage.dto.FoodDTO;
import javaframework.order_food_manage.dto.OrderDetailDTO;
import javaframework.order_food_manage.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class RestoreController{

    @GetMapping("/restore")
    public String showList(Model model, HttpSession session,
                           @RequestParam(name = "table", defaultValue = "") String table) {
        AbstractDTO abstractDTO = new AbstractDTO();
        abstractDTO.setTable(table);
        model.addAttribute("listTableName", new ArrayList<>(Arrays.asList("food","category","image","group","order","user","role","contact")));
        model.addAttribute("abstractDTO",abstractDTO);
        session.setAttribute(SystemConstant.SESSION_ACTIVE,"restore");
        return "views/admin/restore/restore_management";
    }

}
