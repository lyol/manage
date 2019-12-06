package com.lyl.layuiadmin.controller;

import com.lyl.layuiadmin.pojo.system.SysUserDO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

public class BaseController {
    public static Subject getSubjct() {
        return SecurityUtils.getSubject();
    }
    public static SysUserDO getUser() {
        Object object = getSubjct().getPrincipal();
        return (SysUserDO)object;
    }
    public static String getUserId() {
        return getUser().getId();
    }
    public static String getUserName() {
        return getUser().getUserName();
    }
    public static void logout() {
        getSubjct().logout();
    }
}
