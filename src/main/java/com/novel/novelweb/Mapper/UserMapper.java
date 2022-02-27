package com.novel.novelweb.Mapper;

import com.novel.novelweb.entiy.User;
import com.novel.novelweb.entiy.novelShow;
import com.novel.novelweb.entiy.novelcollect;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
@Mapper
@Repository
public interface UserMapper {
    List<User> findAll();
    User findByName(String name);
    String findPswByName(String UserName);
    void save(User user);
    void collect(novelShow novelShow);
    void save_list(novelcollect novelcollect);
    List<novelcollect> getRankList();
    void addNumber(novelcollect novelcollect);
    novelcollect findRankByName(String name);
    List<novelShow> find_collect();

}
