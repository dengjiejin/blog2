package com.djj.blog.config;

import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @auyhor:dengjiejin
 * @data:2022/1/6 15:36
 * @Description:
 */
@Configuration
public class ShiroConfig {

    @Bean(name = "shiroFilterFactoryBean")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(@PathVariable("securityManager")DefaultWebSecurityManager securityManager){
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        //设置安全管理器
        bean.setSecurityManager(securityManager);
        //添加shiro内置过滤器

        Map<String,String> map = new LinkedHashMap<>();
        //授权：必须是user且拥有list权限
//        map.put("/admin/**","authc");
        map.put("/admin/update/**","authc");
        map.put("/admin/save","authc");
        map.put("/admin/write","authc");
        map.put("/admin/delete/*","authc");
        map.put("/admin","authc");
        map.put("/admin/noauth","authc");
        map.put("/admin/update/**","perms[user:update]");

        bean.setFilterChainDefinitionMap(map);

        //设置登录权限
        bean.setLoginUrl("/admin/login");
        //未授权页面
        bean.setUnauthorizedUrl("/admin/noauth");


        return bean;
    }

    @Bean(name = "securityManager")
    public DefaultWebSecurityManager defaultWebSecurityManager(@PathVariable("userRealm") UserRealm userRealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm);
        return securityManager;
    }

    @Bean(name = "userRealm")
    public UserRealm userRealm(){
        return new UserRealm();
    }
}
