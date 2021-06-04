package javaframework.order_food_manage.controller.client.api;

import javaframework.order_food_manage.dto.ContactDTO;
import javaframework.order_food_manage.dto.FoodDTO;
import javaframework.order_food_manage.service.impl.ContactService;
import javaframework.order_food_manage.service.impl.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiClient {

    @Autowired
    private ContactService contactService;

    @Autowired
    private FoodService foodService;

//    Save contact of user
    @PostMapping(value = "/contact",consumes = "application/json", produces = "application/json")
    public ContactDTO addContact(@RequestBody ContactDTO contactDTO) {
        return contactService.save(contactDTO);
    }

    @PostMapping(value = "/list/food")
    public String displayFoodByList( Model model,
                                     @RequestParam(name = "typeShow") String typeShow,
                                     @RequestParam(name = "category_code", required = false) String category_code,
                                     @RequestParam(name = "page", defaultValue = "1", required = true) int page,
                                     @RequestParam(name = "idSort", defaultValue = "0", required = true) int idSort,
                                     @RequestParam(name = "filterPrice", defaultValue = "0", required = true) int filterPrice){
        FoodDTO foodDTO = foodService.getFoodByCategoryCodeAndPagination(category_code, page, 12, true);
        if( idSort != 0) foodDTO = foodService.sorter(foodDTO, idSort);
        if( filterPrice != 0) foodDTO = foodService.filterPrice(foodDTO,filterPrice);
        model.addAttribute("FoodDTOByCategoryCode",foodDTO);

        if( typeShow.equals("list"))
            return "views/client/foodbycategorycode/displayFoodList";
        else {
            return "views/client/foodbycategorycode/displayFoodGrid";
        }
    }



}
