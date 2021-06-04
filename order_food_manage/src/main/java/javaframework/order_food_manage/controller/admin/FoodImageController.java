package javaframework.order_food_manage.controller.admin;

import javaframework.order_food_manage.constant.SystemConstant;
import javaframework.order_food_manage.dto.ImageDTO;
import javaframework.order_food_manage.service.impl.FoodService;
import javaframework.order_food_manage.service.impl.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class FoodImageController implements IAdminController<ImageDTO> {

    @Autowired
    private ImageService imageService;

    @Autowired
    private FoodService foodService;

    @GetMapping("/image")
    @Override
    public String showList(Model model, HttpSession session,
                           @RequestParam(defaultValue = "1") int page,
                           @RequestParam(defaultValue = "5") int limit,
                           @RequestParam(defaultValue = "") String search) {
        ImageDTO imageDTO = imageService.getAllPagination(page, limit, search,true);
        model.addAttribute("imageDTO", imageDTO);
        session.setAttribute(SystemConstant.SESSION_ACTIVE,"image");
        session.setAttribute(SystemConstant.SESSION_AUTHORITY, SecurityContextHolder.getContext().getAuthentication());
        return "views/admin/image/image_management";
    }

    @GetMapping(value={"/image/create","/image/{id}"})
    @Override
    public String showEditOrAddPage(Model model, @PathVariable(name = "id", required = false) Long id) {
        ImageDTO imageDTO = new ImageDTO();
        if( id != null ) {
            imageDTO = imageService.findOne(id);
        }
        model.addAttribute("imageDTO",imageDTO);
        model.addAttribute("foodList",foodService.findAll());
        return "views/admin/image/editImage";
    }

    @GetMapping("/image/detail/{id}")
    @Override
    public String showDetail(Model model, @PathVariable(name = "id", required = false) Long id) {
        ImageDTO imageDTO = new ImageDTO();
        if( id != null ) {
            imageDTO = imageService.findOne(id);
        }
        model.addAttribute("imageDTO",imageDTO);
        model.addAttribute("foodList",foodService.findAll());
        return "views/admin/image/detailImage";
    }
}
