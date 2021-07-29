package com.jerry.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jerry.dao.UserDao;
import com.jerry.entity.User;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

/**
 * @author Cjl
 * @date 2021/7/22 14:14
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserDao userDao;

    @Value("${realPath}")
    private String realPath;

    @RequestMapping("/login")
    @ResponseBody()
    public String login(){
        return "hello";
    }

    //查询所有英雄
    @GetMapping("/users")
    public String users(Model model,Integer pageNo){
        pageNo = pageNo==null?1:pageNo;
        PageHelper.startPage(pageNo,6);
        List<User> users = userDao.findAll();
        PageInfo pageInfo = new PageInfo(users);
        List list = pageInfo.getList();
        model.addAttribute("users",list);
        model.addAttribute("pageInfo",pageInfo);
        return "showUsers";
    }
    //选择英雄，用于更新
    @GetMapping("/user/{cardId}")
    public String selectUserByCardId(@PathVariable Integer cardId,Model model){
        User user = userDao.findByCardId(cardId);
        System.out.println("user = " + user);
        model.addAttribute("user",user);
        return "updateUser";
    }
    //更新英雄
    @PutMapping("/user")
    public String updateUser(Model model, User user, HttpServletRequest request, MultipartFile image) throws IOException {

        //此步用户判断是否上传了文件，若上传了文件则上传至服务器
        if (!image.isEmpty()) {
            //获取原图片，用于删除原图片
            String originImg = user.getImg();
            File originFile = new File(realPath+"\\"+originImg);
            if (originFile.exists()&&!originImg.equals("img1.jpg")){
                originFile.delete();
            }
            String originalFilename = image.getOriginalFilename();
            String uuid = UUID.randomUUID().toString();
            String uniqueFileName = uuid+"-"+originalFilename;
            //image.transferTo(new File(realPath+"\\"+uniqueFileName));
            FileUtils.copyInputStreamToFile(image.getInputStream(), new File(realPath, uniqueFileName));
            System.out.println("图片上传成功！");
            user.setImg(uniqueFileName);
        }
        userDao.updateAllInfos(user);
        return "redirect:/user/users";
    }
    //删除英雄
    @DeleteMapping("/user/{cardId}")
    public String deleteUserByCardId(@PathVariable Integer cardId){
        User user = userDao.findByCardId(cardId);
        //获取原图片，用于删除原图片
        String originImg = user.getImg();
        File originFile = new File(realPath+"\\"+originImg);
        if (originFile.exists()&&!originImg.equals("img1.jpg")){
            originFile.delete();
        }
        userDao.deleteByCardId(cardId);
        return "redirect:/user/users";
    }
    //新增英雄
    @PostMapping("/user")
    public String newUser(User user,MultipartFile image) throws IOException {
        //此步用户判断是否上传了文件，若上传了文件则上传至服务器
        if (!image.isEmpty()) {

            String originalFilename = image.getOriginalFilename();
            String uuid = UUID.randomUUID().toString();
            String uniqueFileName = uuid+"-"+originalFilename;
            //image.transferTo(new File(realPath+"\\"+uniqueFileName));
            FileUtils.copyInputStreamToFile(image.getInputStream(), new File(realPath, uniqueFileName));
            System.out.println("图片上传成功！");
            user.setImg(uniqueFileName);
        }else{
            user.setImg("img1.jpg");
        }
        userDao.insert(user);
        return "redirect:/user/users";
    }

}
