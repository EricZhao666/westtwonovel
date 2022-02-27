package com.novel.novelweb.Controller;


import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSONObject;
import com.novel.novelweb.Mapper.NovelMapper;
import com.novel.novelweb.entiy.novel;
import com.novel.novelweb.entiy.novelShow;
import com.novel.novelweb.entiy.noveldownload;
import com.novel.novelweb.entiy.picture;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Controller
@RequestMapping("/file")
@Slf4j
@Api("文件的下载和上传")
public class FileController {
    @Autowired
    NovelMapper novelMapper;

    @Value("${file.upload.dir}")
    private String uploadFilePath;

    @Value("E:/fileUpload/image")
    private String ImagePath;


    //上传txt文件
    @ApiOperation("上传txt文件到管理员处")
    @ResponseBody
    @PostMapping("/uploadFile")
    public Object fileUpload(@RequestParam("file") MultipartFile file ,@RequestParam("writer") String writer,@RequestParam("introduction") String introduction,@RequestParam("type") String type) throws IOException {
        JSONObject result=new JSONObject();
        if (file.isEmpty()){
            result.put("error","空文件");
            return result;
        }
        String fileName = file.getOriginalFilename();//得到文件名
        String suffixName=fileName.substring(fileName.lastIndexOf("."));//得到后缀名
        String realFileName=fileName.substring(0,fileName.lastIndexOf(suffixName));//得到不包含后缀的文件名
        String fileUrl = uploadFilePath+"/"+fileName;
        File tempfile=new File(fileUrl);
        if (!tempfile.getParentFile().exists()) {
            tempfile.getParentFile().mkdirs();
        }

        try {
            file.transferTo(tempfile);
        }catch (Exception e){
            log.error("发生错误: {}",e);
            result.put("error",e.getMessage());
            return result;

        }
        novel novel=new novel(realFileName,introduction,writer,fileUrl,type);
        novelMapper.uploadChNovel(novel);
        result.put("success","文件上传审核成功");
        return result;
    }


    @ApiOperation("上传图片文件到管理员处")
    @ResponseBody
    @PostMapping("/uploadImage")
    public Object ImageUpload(@RequestParam("file") MultipartFile file ) throws IOException {
        JSONObject result=new JSONObject();
        if (file.isEmpty()){
            result.put("error","空文件");
            return result;
        }
        String fileName = file.getOriginalFilename();//得到文件名
        String suffixName=fileName.substring(fileName.lastIndexOf("."));//得到后缀名
        String realFileName=fileName.substring(0,fileName.lastIndexOf(suffixName));//得到不包含后缀的文件名
        String fileUrl = ImagePath+"/"+fileName;
        File tempfile=new File(fileUrl);
        if (!tempfile.getParentFile().exists()) {
            tempfile.getParentFile().mkdirs();
        }

        try {
            file.transferTo(tempfile);
        }catch (Exception e){
            log.error("发生错误: {}",e);
            result.put("error",e.getMessage());
            return result;

        }
        picture picture=new picture(realFileName,fileUrl);
        novelMapper.uploadImage(picture);
        result.put("success","图片上传审核成功");
        return result;
    }


    //下载文件
    @ApiOperation("下载小说")
    @ResponseBody
    @GetMapping("/downloadFile")
    public Object fileDownload(HttpServletResponse response,@RequestParam("name") String fileName) throws IOException {
        JSONObject result=new JSONObject();
        noveldownload noveldownload=novelMapper.findUrl(fileName);
        File file = new File(noveldownload.getUrl());
        String downloadName=file.getName();
        if (!file.exists()){
            result.put("error","文件不存在");
            return result.toString();

        }
        response.reset();
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setContentLength((int)file.length());
        response.setHeader("Content-Disposition","attachment;filename="+java.net.URLEncoder.encode(downloadName, "UTF-8"));

        byte[] readBytes= FileUtil.readBytes(file);
        OutputStream os=response.getOutputStream();
        os.write(readBytes);
        result.put("success","下载成功");
        return result;
    }

    @ApiOperation("查找所有图书")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public List<novelShow> findAllCh(){
        List<novelShow> list=novelMapper.findNovel();
        return list;
    }

    @ApiOperation("模糊搜索")
    @PostMapping("/searchNovel/{name}")
    @ResponseBody
    public List<novelShow> findNovel(@RequestParam("name") String name){
        List<novelShow> list=novelMapper.findByName(name);
        return list;
    }
}
