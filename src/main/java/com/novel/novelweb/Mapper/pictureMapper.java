package com.novel.novelweb.Mapper;

import com.novel.novelweb.entiy.picture;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface pictureMapper {
    List<picture> findAllCh();
    List<picture> findAll();
    void savePicCheck(picture picture);
    void  savePic(picture picture);
    void deletePicCheck(String name);
    picture findByName(String name);
}
