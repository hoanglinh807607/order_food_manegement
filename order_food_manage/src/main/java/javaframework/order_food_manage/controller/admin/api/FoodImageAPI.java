package javaframework.order_food_manage.controller.admin.api;

import javaframework.order_food_manage.dto.ImageDTO;
import javaframework.order_food_manage.dto.UserDTO;
import javaframework.order_food_manage.service.impl.FoodService;
import javaframework.order_food_manage.service.impl.ImageService;
import javaframework.order_food_manage.service.impl.RoleService;
import javaframework.order_food_manage.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/admin/api")
public class FoodImageAPI implements IAdminAPI<ImageDTO> {

    @Autowired
    private ImageService imageService;

    @Autowired
    private FoodService foodService;

    @Value("${UPLOAD_FOLDER}")      // value cho phép map dữ liệu từ file properties vào trong biến trong java
    String upload_folder;

    @PostMapping("/image")
    public String insertImage(Model model, @ModelAttribute ImageDTO obj,
                              @RequestParam(name = "imagePreview") MultipartFile filePreview,
                              @RequestParam(name = "image") MultipartFile[] files) {
        if( !filePreview.isEmpty()  ){
            Path pathPreview = Paths.get(upload_folder+filePreview.getOriginalFilename());
            writeImage(pathPreview, filePreview);
            obj.setPath(filePreview.getOriginalFilename());
            obj.setIs_preview(true);
            imageService.save(obj);
        }
        for ( MultipartFile file : files) {
            if( !file.isEmpty() ){
                if( !file.getOriginalFilename().equals("0")) {
                    ImageDTO imageDTO = new ImageDTO();
                    imageDTO.setFoodId(obj.getFoodId());
                    imageDTO.setIs_preview(false);
                    Path path = Paths.get(upload_folder+file.getOriginalFilename());
                    writeImage(path, file);
                    imageDTO.setPath(file.getOriginalFilename());
                    imageService.save(imageDTO);
                }
            }
        }
        obj.setAlert("success");
        obj.setMessage("Insert success");
        model.addAttribute("imageDTO", obj);
        model.addAttribute("foodList",foodService.findAll());
        return "views/admin/image/editImage";
    }

    private void writeImage(Path path, MultipartFile file){
        try{
            byte[] bytes = file.getBytes();
            Files.write(path,bytes);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public String doAdd(Model model, @ModelAttribute ImageDTO obj) {
        return "";
    }

    @Override
    public String doEdit(Model model, @ModelAttribute ImageDTO obj) {
        return "";
    }

    @DeleteMapping("/image")
    @ResponseBody
    @Override
    public String doDelete(@RequestBody ImageDTO obj) {
        imageService.delete(obj.getIds());
        return "success";
    }
}
