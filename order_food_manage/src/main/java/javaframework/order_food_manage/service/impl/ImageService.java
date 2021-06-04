package javaframework.order_food_manage.service.impl;

import javaframework.order_food_manage.converter.ImageConverter;
import javaframework.order_food_manage.dto.ImageDTO;
import javaframework.order_food_manage.entities.ImageEntity;
import javaframework.order_food_manage.repository.ImageRepos;
import javaframework.order_food_manage.service.IImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ImageService implements IImageService {

    @Autowired
    private ImageConverter imageConverter;

    @Autowired
    private ImageRepos imageRepos;

    @Override
    public List<ImageDTO> findAll() {
        List<ImageDTO> imageDTOS = new ArrayList<>();
        List<ImageEntity> imageEntities = (List<ImageEntity>) imageRepos.findAll();
        imageEntities.forEach(categoryEntity -> {
            imageDTOS.add(imageConverter.toDto(categoryEntity));
        });
        return imageDTOS;
    }

    @Override
    public ImageDTO save(ImageDTO imageDTO) {
        ImageEntity imageEntity = new ImageEntity();
        ImageDTO image = new ImageDTO();
        boolean checkInsert = false;
        try {
            if (imageDTO.getId() != null) {
                ImageEntity entity_old = imageRepos.findById(imageDTO.getId()).get();
                imageEntity = imageConverter.toEntity( entity_old, imageDTO);
            } else {
                imageEntity = imageConverter.toEntity(imageDTO);
                checkInsert = true;
            }
            image = imageConverter.toDto(imageRepos.save(imageEntity));
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if( image.getId() != null ){
                image.setAlert("success");
                if( checkInsert ) {
                    image.setMessage("Insert success");
                }else{
                    image.setMessage("Update success");
                }
            }else{
                image.setAlert("danger");
                image.setMessage("No success");
            }
        }
        return image;
    }

//    @Override
//    public void delete(Long[] ids) {
//        for (Long item : ids) {
//            imageRepos.deleteById(item);
//        }
//    }

    @Override
    public void delete(Long[] ids) {
        for (Long item : ids) {
            ImageEntity entity = imageRepos.findById(item).get();
            entity.setStatus(false);
            imageRepos.save(entity);
        }
    }

    @Override
    public void permanentDelete(Long[] ids) {
        for (Long item : ids) {
            imageRepos.deleteById(item);
        }
    }

    @Override
    public void restore(Long[] ids) {
        for (Long item : ids) {
            ImageEntity entity = imageRepos.findById(item).get();
            entity.setStatus(true);
            imageRepos.save(entity);
        }
    }

    @Override
    public ImageDTO getAllPagination(int page, int limit, String keyword, Boolean status) {
        ImageDTO imageDTO = new ImageDTO();
        List<ImageEntity> imageEntities = new ArrayList<>();
        int total = 0;
        if (limit == 1) {
            if( !keyword.equals("") ){
                imageEntities = (List<ImageEntity>) imageRepos.findAllSearch(keyword,status);
            }else {
                imageEntities = (List<ImageEntity>) imageRepos.findAll();
            }
        } else {
            if( !keyword.equals("")){
                imageEntities = imageRepos.findAllSearch(keyword, PageRequest.of(page-1, limit),status);
                total = (int) Math.ceil((double) countSearch(keyword) / limit);
            }else {
                imageEntities = imageRepos.findAll(PageRequest.of(page - 1, limit),status);
                total = (int) Math.ceil((double) totalItem(status) / limit);
            }
        }
        imageEntities.forEach(result -> {
            imageDTO.getResultList().add(imageConverter.toDto(result));
        });
        imageDTO.setTotalPage(total);
        imageDTO.setPage(page);
        imageDTO.setLimit(limit);
        imageDTO.setSearch(keyword);
        return imageDTO;
    }

    @Override
    public int totalItem(Boolean status) {
        return (int) imageRepos.count(status);
    }

    @Override
    public int countSearch(String keySearch) {
        return imageRepos.countSearch(keySearch);
    }

    @Override
    public ImageDTO findOne(Long id) {
        return imageConverter.toDto(imageRepos.findById(id).get());
    }

    @Override
    public List<ImageDTO> findByFoodId(Long id) {
        List<ImageEntity> entities = imageRepos.findByFoodId(id);
        List<ImageDTO> imageDTOS = new ArrayList<>();
        entities.forEach(e->{imageDTOS.add(imageConverter.toDto(e));});
        return imageDTOS;
    }
}
