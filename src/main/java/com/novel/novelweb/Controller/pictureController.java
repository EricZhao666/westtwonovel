package com.novel.novelweb.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.novel.novelweb.Mapper.pictureMapper;
import com.novel.novelweb.component.JsonUtils;
import com.novel.novelweb.entiy.picture;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/file")
@Slf4j
@Api("图片上传的检查")
public class pictureController {
    @Autowired
    pictureMapper pictureMapper;

    @Value("E:/fileUpload/image")
    private String ImageUpload;

    @ApiOperation("管理员处的所有审核文件的地址")
    @RequestMapping(value = "/findAllChPic", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public List<picture> findAllCh(){
        List<picture> list=pictureMapper.findAllCh();
        return list;
    }

    @ApiOperation("用户看到的所有图片地址")
    @RequestMapping(value = "/findAllPic", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public List<picture> findAll(){
        List<picture> list=pictureMapper.findAll();
        return list;
    }

    @ApiOperation("上传pic文件到审核")
    @ResponseBody
    @PostMapping("/picCheckUp")
    public Object fileUpload(@RequestParam("file") MultipartFile file ) throws IOException {
        JSONObject result=new JSONObject();
        if (file.isEmpty()){
            result.put("error","空文件");
            return result;
        }
        String fileName = file.getOriginalFilename();//得到文件名
        String suffixName=fileName.substring(fileName.lastIndexOf("."));//得到后缀名
        String realFileName=fileName.substring(0,fileName.lastIndexOf(suffixName));//得到不包含后缀的文件名
        String fileUrl = ImageUpload+"/"+fileName;
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
        pictureMapper.savePicCheck(picture);
        result.put("success","图片上传成功");
        return result;
    }

    @ApiOperation("管理员审核图片")
    @GetMapping("/checkPic/{name}")
    public Object successChecked(@PathVariable("name")String name){
        picture picture=pictureMapper.findByName(name);
        pictureMapper.savePic(picture);
        pictureMapper.deletePicCheck(name);
        return JSON.toJSONString(new JsonUtils(1,"审核加入成功"));

    }

    @ApiOperation("管理员删除不通过审核的图片")
    @GetMapping("/delete/{name}")
    public Object delete(@PathVariable("name")String name){
        picture picture=pictureMapper.findByName(name);
        pictureMapper.deletePicCheck(name);
        return JSON.toJSONString(new JsonUtils(1,"删除成功"));
    }



}
