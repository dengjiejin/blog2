package com.djj.blog.config;

import com.djj.blog.entity.User;
import com.djj.blog.service.UserService;
import org.apache.catalina.security.SecurityUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;

import javax.annotation.Resource;

/**
 * @auyhor:dengjiejin
 * @data:2022/1/6 15:33
 * @Description:
 */
public class UserRealm extends AuthorizingRealm {

    @Resource
    private UserService userService;

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("执行授权！");
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//        info.addStringPermission("user:add");
        //从认证拿到用户对象
        Subject subject = SecurityUtils.getSubject();
        User currentUser = (User)subject.getPrincipal();

        //授权 从数据库中获取
        info.addStringPermission(currentUser.getPerms());
        return info;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("执行认证！");
        //前端传值封装的token,token是全局
        UsernamePasswordToken userToken = (UsernamePasswordToken) token;
        //从数据库中获取用户名和密码
        User user = userService.findUserByUsername(userToken.getUsername());
        if(user==null){
            return null;  //抛出异常，UnknowAccountException
        }
        //密码认证，shiro做，避免密码泄露（密码默认加密，也可以更换加密算法），第一个参数：传递对象授权，第二个参数：验证密码
        return new SimpleAuthenticationInfo(user,user.getPassword(),"");
    }
}
