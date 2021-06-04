package javaframework.order_food_manage.controller.client;

import javaframework.order_food_manage.constant.SystemConstant;
import javaframework.order_food_manage.dto.ContactDTO;
import javaframework.order_food_manage.dto.FoodDTO;
import javaframework.order_food_manage.dto.UserDTO;
import javaframework.order_food_manage.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Comparator;

@Controller
public class HomeController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private FoodService foodService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;


    @GetMapping(value = {"/index","/"})
    public String showHomePage(HttpSession session, Model model){
        session.setAttribute("categoryList",categoryService.findAll());
        session.setAttribute(SystemConstant.SESSION_ACTIVE,"index");
        session.setAttribute(SystemConstant.SESSION_KEY_CART,orderService.getOrderDto(session)); //Khởi tạo đối tượng rỗng cho giỏ hàng
        session.setAttribute(SystemConstant.SESSION_ACTIVE_MENU_CLIENT, "");    // Đánh dấu khi focus vào menu ngoài client
        session.setAttribute(SystemConstant.SESSION_AUTHORITY,SecurityContextHolder.getContext().getAuthentication());
        if( SecurityContextHolder.getContext().getAuthentication().getName() != "anonymousUser"){
            session.setAttribute("userDTO",userService.findByUserName(SecurityContextHolder.getContext().getAuthentication().getName()));
        }
        model.addAttribute("categoryList",session.getAttribute("categoryList"));    //Chỉ load menu ở trang chủ và hiển thị sản phẩm
        model.addAttribute("listFoodByFoodGroupId",foodService.findByFoodGroupId(3l));
        model.addAttribute("listFoodHavePricePromotion",foodService.findAllHavePricePromotion());
        return "views/client/index";
    }

    @GetMapping("/introduce")
    public String showIntroducePage(HttpSession session){
        session.setAttribute(SystemConstant.SESSION_ACTIVE,"introduce");
        return "views/client/introduce";
    }

    @GetMapping("/contact")
    public String showContactPage(HttpSession session,Model model){
        session.setAttribute(SystemConstant.SESSION_ACTIVE,"contact");
        model.addAttribute("contactDTO",new ContactDTO());
        return "views/client/contact";
    }

    @GetMapping("/list/food")
    public String showListFoodByCategory(HttpSession session,
                                         Model model,
                                         @RequestParam(name = "category_code",required = false) String category_code,
                                         @RequestParam(name = "page", defaultValue = "1",required = false) int page){
        model.addAttribute("categoryList",session.getAttribute("categoryList"));
        model.addAttribute("FoodDTOByCategoryCode",foodService.getFoodByCategoryCodeAndPagination(category_code, 1, 12, true));
        model.addAttribute("byFoodName", Comparator.comparing(FoodDTO::getName));
        session.setAttribute(SystemConstant.SESSION_ACTIVE_MENU_CLIENT, category_code);
        session.setAttribute(SystemConstant.SESSION_ACTIVE,"");
        return "views/client/foodbycategorycode/listfoodbycategory";
    }



    @GetMapping("/shopping-cart")
    public String showCart(HttpSession session){
        // Khi đăng nhập thành công thì cập nhật lại là người đăng nhập
        session.setAttribute(SystemConstant.SESSION_AUTHORITY,SecurityContextHolder.getContext().getAuthentication());
        session.setAttribute(SystemConstant.SESSION_ACTIVE,"shop_cart");
        return "views/client/shopping_cart";
    }

    @GetMapping("/cart/pay")
    public String pay(HttpSession session) {
        return "redirect:/shopping-cart";
    }

    @GetMapping("/food/details/{id}")
    public String showFoodDetails(HttpSession session, Model model,@PathVariable(name = "id") Long id){
        model.addAttribute("foodDto",foodService.findOne(id));
        model.addAttribute("categoryList",session.getAttribute("categoryList"));
        session.setAttribute(SystemConstant.SESSION_ACTIVE,"promotion");
        return "views/client/food_details";
    }

    @RequestMapping(value = "/quick_view_info/{id}") //@RequestBody là mapping với dữ liệu json vào trong dto
    public String showQuickView(Model model, @PathVariable(name = "id") Long id){
        model.addAttribute("foodDto",foodService.findOne(id));
        return "views/client/quick_view";
    }

    @GetMapping("/login")
    public String login(Model model,@RequestParam(name = "username",required = false, defaultValue = "") String username,
                        @RequestParam(name = "alert",required = false, defaultValue = "") String alert,
                        @RequestParam(name = "message",required = false, defaultValue = "") String message
                        ,HttpServletRequest request){
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(username);
        userDTO.setAlert(alert);
        userDTO.setMessage(message);
        model.addAttribute("userDTO",userDTO);
        return "views/login/login";
    }

    @GetMapping("/register")
    public String register(){
        return "views/login/register";
    }


    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse resp) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, resp, auth);
        }
        return "redirect:/index";
    }

}
