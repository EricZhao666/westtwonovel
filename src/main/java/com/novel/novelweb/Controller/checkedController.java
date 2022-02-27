package com.novel.novelweb.Controller;

import com.alibaba.fastjson.JSON;
import com.novel.novelweb.Mapper.NovelMapper;
import com.novel.novelweb.Mapper.UserMapper;
import com.novel.novelweb.component.JsonUtils;
import com.novel.novelweb.entiy.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/checked")
@Slf4j
@Api("管理员审核小说")
public class checkedController {
    @Autowired
    NovelMapper novelMapper;
    @Autowired
    UserMapper userMapper;


    @ApiOperation("管理员审核小说")
    @GetMapping("/checkNovel/{name}")
    @ResponseBody
    public Object successChecked(@RequestParam("id")int id){
        novel novel=novelMapper.findChById(id);
        if (novel!=null){
            System.out.println(novel.getName());
            novelShow novelShow=new novelShow(novel.getName(),novel.getIntroduction(),novel.getWriter(),novel.getType());
            noveldownload noveldownload=new noveldownload(novel.getName(),novel.getUrl(),novelMapper.findChIdByName(novel.getName())) ;
            novelMapper.saveShow(novelShow);
            novelMapper.saveDownload(noveldownload);
            novelMapper.deleteChNovel(id);
            novelcollect novelcollect=new novelcollect(novel.getName(),1,novelMapper.findIdByName(novel.getName()));
            userMapper.save_list(novelcollect);
            return JSON.toJSONString(new JsonUtils(1,"审核成功"));
        }else {
            return JSON.toJSONString(new JsonUtils(-1,"未找到图书"));
        }



    }

    @ApiOperation("管理员审核图片")
    @GetMapping("/checkPicture/{name}")
    @ResponseBody
    public Object successPic(@RequestParam("name") String name){
        picture picture=novelMapper.findPicByName(name);
        novelMapper.checkImage(picture);
        deletePic(name);
        return JSON.toJSONString(new JsonUtils(1,"审核成功"));
    }

    @ApiOperation("删除不通过的图片")
    @GetMapping("/deletePic/{name}")
    @ResponseBody
    public Object deletePic(@RequestParam("name") String name){
        picture picture=novelMapper.findPicByName(name);
        novelMapper.deleteImage(picture);
        return JSON.toJSONString(new JsonUtils(1,"删除成功"));
    }

    @ApiOperation("删除不通过的小说")
    @GetMapping("/deleteNovel/{name}")
    @ResponseBody
    public Object deleteNovel(@PathVariable("id")int id){
        novel novel =novelMapper.findChById(id);
        novelMapper.deleteChNovel(id);
        return JSON.toJSONString(new JsonUtils(1,"删除成功"));
    }

    @ApiOperation("查看所有待审核小说")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public List<novel> findAllCh(){
        List<novel> list=novelMapper.findAllCh();
        return list;

    }

    @ApiOperation("查看所有待审核图片")
    @RequestMapping(value = "/findAllChPic", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public List<picture> findAllChPic(){
        List<picture> list=novelMapper.findAllChPic();
        return list;
    }



}
