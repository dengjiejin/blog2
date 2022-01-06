package com.djj.blog.controller;

import com.djj.blog.entity.Article;
import com.djj.blog.service.ArticleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by wangchenghao on 2017/8/2.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {
    @Autowired
    ArticleService articleService;
    @Test
    public void admin(){

        List<Article> articles = articleService.list();

        for (int i=0; i<articles.size(); i++){
            System.out.println(articles.get(i).getId());
        }
    }

}