package com.djj.blog.controller;

import com.djj.blog.aspect.WebSecurityConfig;
import com.djj.blog.service.ArticleService;
import com.djj.blog.service.CategoryService;
import com.djj.blog.entity.Article;
import com.djj.blog.entity.Category;
import com.djj.blog.entity.User;
import com.djj.blog.service.UserService;
import org.apache.catalina.security.SecurityUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by wangchenghao on 2017/8/2.
 */
@Controller
@RequestMapping("/admin")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    ArticleService articleService;
    @Autowired
    CategoryService categoryService;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    /**
     * 后台主页
     */
    @RequestMapping("")
    public String admin(Model model){
        List<Article> articles = articleService.list();
        model.addAttribute("articles", articles);

        return "admin/index";
    }

    /**
     * 登录模块
     * @return
     */
    @RequestMapping("/login")
    public String login(){

        return "admin/login";
    }

    /**
     * 登录验证
     * @param user
     * @param model
     * @return
     */

    @RequestMapping(value = "/dologin", method = RequestMethod.POST)
    public String doLogin(HttpServletResponse response, User user, Model model){
        //获取当前用户对象
        Subject subject = SecurityUtils.getSubject();
        //封装用户登录数据
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        try{
            subject.login(token);
            return  "redirect:/admin";
        }catch (UnknownAccountException e){
            System.out.println("用户名不存在");
            model.addAttribute("error","用户名不存在");
            return "admin/login";
        }catch (IncorrectCredentialsException e){
            System.out.println("密码错误");
            model.addAttribute("error","密码错误");
            return "admin/login";
        }


//        if(userService.login(user.getUsername(), user.getPassword())){
//            Cookie cookie = new Cookie(WebSecurityConfig.SESSION_KEY, user.toString());
//            response.addCookie(cookie);
//            model.addAttribute("user", user);
//            System.out.println(cookie.getName());
//
//            return "redirect:/admin/index";
//        }else {
//            model.addAttribute("error", "用户名或者密码错误");
//            System.out.println("failture");
//
//            return "admin/login";
//        }
    }

    /**
     * 删除博客
     * @param id
     * @return
     */
    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable("id") String id){
        articleService.delete(id);

        return "redirect:/admin";
    }

    @RequestMapping("/write")
    public String write(Model model){
        List<Category> categories = categoryService.list();
        model.addAttribute("categories", categories);
        model.addAttribute("article", new Article());

        return "admin/write";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(Article article){
        //设置种类
        String name = article.getCategory().getName();
        Category category = categoryService.fingdByName(name);
        article.setCategory(category);
        //设置摘要,取前40个字
        if(article.getContent().length() > 40){
            article.setSummary(article.getContent().substring(0, 40));
        }else {
            article.setSummary(article.getContent().substring(0, article.getContent().length()));
        }
        article.setDate(sdf.format(new Date()));
        articleService.save(article);

        return "redirect:/admin";
    }

    @RequestMapping("/update/{id}")
    public String update(@PathVariable("id") String id, Model model){
        Article article = articleService.getById(id);
        model.addAttribute("target", article);
        List<Category> categories = categoryService.list();
        model.addAttribute("categories", categories);
        model.addAttribute("article", new Article());

        return "admin/update";
    }
    //未授权页面
    @RequestMapping("noauth")
    @ResponseBody
    public String noauthor(Model model){
        return "未授权页面";
    }
}
