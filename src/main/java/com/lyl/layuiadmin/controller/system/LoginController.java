package com.lyl.layuiadmin.controller.system;

import com.lyl.layuiadmin.common.Result;
import com.lyl.layuiadmin.controller.BaseController;
import com.lyl.layuiadmin.enums.ResultEnum;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 登录
 */
@Controller
public class LoginController extends BaseController {

    @GetMapping("/login") 
    public String login() {
        if (getUser() != null) {
            return "redirect:/";
        }
        return "login";
    }

    @PostMapping("/login")
    @ResponseBody
    public Result login(String username, String password, Boolean rememberMe) {
        if (rememberMe == null) {
            rememberMe = false;
        }
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        Subject subject = SecurityUtils.getSubject();
        try {
            token.setRememberMe(rememberMe);
            subject.login(token);
        } catch (Exception e) {
            return Result.build(ResultEnum.UNKONW_ERROR.getCode(), e.getMessage());
        }
        return Result.ok();
    }
}
