package com.lyl.layuiadmin.controller.system;

import com.lyl.layuiadmin.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController extends BaseController {

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("nickName", getUser().getNickName());
        return "index";
    }

}
