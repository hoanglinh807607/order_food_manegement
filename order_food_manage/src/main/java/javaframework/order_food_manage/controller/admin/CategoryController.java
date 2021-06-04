package javaframework.order_food_manage.controller.admin;

import javaframework.order_food_manage.constant.SystemConstant;
import javaframework.order_food_manage.dto.CategoryDTO;
import javaframework.order_food_manage.service.impl.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class CategoryController implements IAdminController<CategoryDTO> {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/category")
    @Override
    public String showList(Model model, HttpSession session,
                           @RequestParam(defaultValue = "1") int page,
                           @RequestParam(defaultValue = "5") int limit,
                           @RequestParam(defaultValue = "") String search) {
        CategoryDTO categoryDTO = categoryService.getAllPagination(page, limit, search,true);
        model.addAttribute("categoryDTO", categoryDTO);
        session.setAttribute(SystemConstant.SESSION_ACTIVE,"category");
        return "views/admin/category/category_management";
    }

    @GetMapping(value={"/category/create","/category/{id}"})
    @Override
    public String showEditOrAddPage(Model model, @PathVariable(name = "id", required = false) Long id) {
        CategoryDTO categoryDTO = new CategoryDTO();
        if( id != null ) {
            categoryDTO = categoryService.findOne(id);
        }
        model.addAttribute("categoryDTO",categoryDTO);
        return "views/admin/category/editCategory";
    }

    @GetMapping(value={"/category/detail/{id}"})
    @Override
    public String showDetail(Model model, @PathVariable(name = "id", required = false) Long id) {
        CategoryDTO categoryDTO = new CategoryDTO();
        if( id != null ) {
            categoryDTO = categoryService.findOne(id);
        }
        model.addAttribute("categoryDTO",categoryDTO);
        return "views/admin/category/detailCategory";
    }
}
