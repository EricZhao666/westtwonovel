package com.novel.novelweb.Controller;

import com.alibaba.fastjson.JSON;
import com.novel.novelweb.Mapper.NovelMapper;
import com.novel.novelweb.Mapper.UserMapper;
import com.novel.novelweb.component.JsonUtils;
import com.novel.novelweb.entiy.User;
import com.novel.novelweb.entiy.novelCommend;
import com.novel.novelweb.entiy.novelShow;
import com.novel.novelweb.entiy.novelcollect;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@Api("登录注册")
public class UserController {
    @Autowired
    UserMapper userMapper;
    @Autowired
    NovelMapper novelMapper;

    //登录
    @ApiOperation("登录方法")
    @RequestMapping(value = "/login", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object login(@RequestParam("username") String name,
                        @RequestParam("password") String pwd,
                        HttpServletResponse response,
                        HttpSession session){

        User user=userMapper.findByName(name);

        if (user!=null){
            if (pwd.equals(user.getPassword())){
                String authority=user.getPower();
                Cookie cookie=new Cookie("username",name);
                response.addCookie(cookie);
                session.setAttribute("username",name);
                //判断权限，1是管理员，0是用户
                if (authority.equals("vip")){
                    return JSON.toJSONString(new JsonUtils(1,"登录成功"));
                }else {
                    return JSON.toJSONString(new JsonUtils(0,"登录成功"));
                }
            }
            else{
                return JSON.toJSONString(new JsonUtils(-1,"密码错误"));
            }
        }
        else {
            return  JSON.toJSONString(new JsonUtils(-1,"用户名不存在"));
        }

    }

    //注册
    @ApiOperation("注册方法")
    @PostMapping("/register")
    @ResponseBody
    public Object register(@RequestParam("username") String name, @RequestParam("password") String pwd){
        User user=new User(name,pwd,"customer");
        User findUser=userMapper.findByName(name);
        if (findUser!=null){
            return JSON.toJSONString(new JsonUtils(-1,"用户名已被占用"));
        }
        else {
            userMapper.save(user);
            return JSON.toJSONString(new JsonUtils(0,"注册成功"));
        }

    }

    //添加到收藏夹
    @GetMapping("/CollectNovel/{name}")
    @ApiOperation("收藏小说")
    @ResponseBody
    public Object collect(@PathVariable("name")String name){
        novelShow novel=novelMapper.findByNameTure(name);
        userMapper.collect(novel);
        add_number(name);
        return JSON.toJSONString(new JsonUtils(0,"收藏成功"));

    }



    @ApiOperation("收藏量前12")
    @RequestMapping(value = "/findRank", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public List<novelcollect> findRankList(){
        List<novelcollect> list=userMapper.getRankList();
        Collections.sort(list);
        List<novelcollect> list1=new ArrayList<novelcollect>();
        for (int i = list.size()-1; i >list.size()- 12; i--) {
            novelcollect novelcollect=list.get(i);
            list1.add(novelcollect);
        }
        return list1;
        }


    public void add_number(String name){
        novelcollect novelcollect=userMapper.findRankByName(name);
        int reader=novelcollect.getReader()+1;
        novelcollect.setReader(reader);
        userMapper.addNumber(novelcollect);
    }

    @ApiOperation("获取推荐小说")
    @RequestMapping(value = "/findCommend", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public List<novelCommend> getCommend(){
        List<novelShow> list=novelMapper.findNovel();
        Random random=new Random();
        int commend=random.nextInt(10);
        System.out.println(commend);
        novelShow novelShow=list.get(commend);
        System.out.println(novelMapper.findIdByName(novelShow.getName()));
        novelCommend novelCommend=new novelCommend(novelShow.getName(),novelShow.getType(),novelMapper.findIdByName(novelShow.getName()));
        List<novelCommend> list1=new ArrayList<novelCommend>();
        list1.add(novelCommend);
        return list1;
    }

    @ApiOperation("获取收藏夹")
    @ResponseBody
    @RequestMapping(value = "/findCollectAll", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public List<novelShow> getSelfCollect(){
        List<novelShow> list=userMapper.find_collect();
        return list;
    }

    @ApiOperation("判断是否登录")
    @ResponseBody
    @RequestMapping(value = "/isLogin", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public Object isLogin(HttpServletResponse response, HttpServletRequest request){
        Object logUser=request.getSession().getAttribute("username");
        if (logUser==null){
            return JSON.toJSONString(new JsonUtils(-1,"尚未登录"));
        }else {
            return JSON.toJSONString(new JsonUtils(1,"已登录"));
        }

    }



}




