package com.lyl.layuiadmin.config;

import com.lyl.layuiadmin.Dao.system.SysUserDao;
import com.lyl.layuiadmin.common.ApplicationContextRegister;
import com.lyl.layuiadmin.util.DateUtils;
import com.lyl.layuiadmin.util.Md5Util;
import com.lyl.layuiadmin.pojo.system.SysUserDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.Date;
import java.util.Set;

@Slf4j
public class UserRealm extends AuthorizingRealm {
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
        String userName = ((SysUserDO)SecurityUtils.getSubject().getPrincipal()).getUserName();
        // TODO 获取用户权限
        // MenusService menuService = ApplicationContextRegister.getBean(MenusService.class);
        // Set<String> perms = menuService.listPerms(userName);
        Set<String> perms = null;
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(perms);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String) token.getPrincipal();
        String password = new String((char[]) token.getCredentials());
        SysUserDO user = null;
        SysUserDao sysUserDao = ApplicationContextRegister.getBean(SysUserDao.class);
        Boolean loginError = false;
        try {
            if (StringUtils.isEmpty(username) && StringUtils.isEmpty(password)) {
                throw new UnknownAccountException("账号或密码不正确");
            }
            user = sysUserDao.findSysUserByUserName(username);
            if (user == null) {
                throw new UnknownAccountException("账号未授权，请联系管理员处理！");
            }
            if (user.getLoginErrorCount() != null && user.getLoginErrorCount() >= 5 && user.getLastLoginErrorDay() != null
                    && DateUtils.countDaysBetweenTwoDate(user.getLastLoginErrorDay(), new Date()) == 0) {
                throw new UnknownAccountException("密码输入错误5次，账户锁定！请联系管理员！");
            }
            String userPassword = Md5Util.MD5(password).toLowerCase();
            if (!StringUtils.equals(user.getPassword().toLowerCase(), userPassword)) {
                loginError = true;
                throw new UnknownAccountException("账号或密码不正确");
			}
        } catch (UnknownAccountException e) {
           log.error(e.getMessage(),e);
           throw new UnknownAccountException(e.getMessage());
        } catch (Exception e) {
           log.error(e.getMessage(),e);
           throw new UnknownAccountException("登录异常，请联系管理员处理！");
        }finally {
            if (loginError){
                sysUserDao.updateLoginErrorCount(user);
            }
        }

        //不使用shiro自带的密码验证
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, password, getName());
        return info;
    }

}

