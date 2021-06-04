package javaframework.order_food_manage.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class UploadFolderConfig implements WebMvcConfigurer {
    @Value("${UPLOAD_FOLDER}")      // value cho phép map dữ liệu từ file properties vào trong biến trong java
    String upload_folder;
    @Override // dấu ** được hiểu là map tất cả file trong thư mục góc vào thư mục ảo. image_upload là thư mục ảo
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("image_upload/**").addResourceLocations("file:"+upload_folder);
    }
}
