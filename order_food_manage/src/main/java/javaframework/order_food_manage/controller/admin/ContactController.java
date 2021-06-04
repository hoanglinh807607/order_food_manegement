package javaframework.order_food_manage.controller.admin;

import javaframework.order_food_manage.constant.SystemConstant;
import javaframework.order_food_manage.dto.CategoryDTO;
import javaframework.order_food_manage.dto.ContactDTO;
import javaframework.order_food_manage.service.impl.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @GetMapping("/contact")
    public String showList(Model model, HttpSession session,
                           @RequestParam(defaultValue = "1") int page,
                           @RequestParam(defaultValue = "5") int limit,
                           @RequestParam(defaultValue = "") String search) {
        ContactDTO contactDTO = contactService.getAllPagination(page, limit, search,true);
        model.addAttribute("contactDTO", contactDTO);
        session.setAttribute(SystemConstant.SESSION_ACTIVE,"contact");
        return "views/admin/contact/contact_management";
    }

    @GetMapping(value="/contact/{id}")
    public String showDetailContact(Model model, @PathVariable(name = "id", required = false) Long id) {
        ContactDTO contactDTO = contactService.findOne(id);
        if( contactDTO.getState() == 0){
            contactDTO.setState(1);
            contactDTO = contactService.save(contactDTO);
        }
        model.addAttribute("contactDTO",contactDTO);
        return "views/admin/contact/detailContact";
    }

}
