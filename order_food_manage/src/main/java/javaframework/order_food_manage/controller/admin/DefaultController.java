package javaframework.order_food_manage.controller.admin;

import javaframework.order_food_manage.constant.SystemConstant;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class DefaultController {

    @RequestMapping("/dashboard")
    public String showDashboard(HttpSession session){
        session.setAttribute(SystemConstant.SESSION_AUTHORITY,SecurityContextHolder.getContext().getAuthentication());
        session.setAttribute(SystemConstant.SESSION_ACTIVE,"dashboard");
        return "views/admin/dashboard";
    }

}
