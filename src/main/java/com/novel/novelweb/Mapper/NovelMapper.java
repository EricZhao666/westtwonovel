package com.novel.novelweb.Mapper;

import com.novel.novelweb.entiy.novel;
import com.novel.novelweb.entiy.novelShow;
import com.novel.novelweb.entiy.noveldownload;
import com.novel.novelweb.entiy.picture;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface NovelMapper {
    List<novel> findAllCh();
    List<novelShow> findNovel();
    void uploadChNovel(novel novel);
    void saveShow(novelShow novelShow);
    void saveDownload(noveldownload noveldownload);
    void deleteChNovel(int id);
    novel findChById(int id);
    List<novelShow> findByName(String name);
    noveldownload findUrl(String name);
    novelShow findByNameTure(String name);
    int findIdByName(String name);
    int findChIdByName(String name);
    void uploadImage(picture picture);
    void checkImage(picture picture);
    void deleteImage(picture picture);
    List<picture> findAllChPic();
    picture findPicByName(String name);




}
