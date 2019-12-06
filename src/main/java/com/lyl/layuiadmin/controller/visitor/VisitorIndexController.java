package com.lyl.layuiadmin.controller.visitor;

import com.lyl.layuiadmin.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/visitor")
public class VisitorIndexController extends BaseController {

    @GetMapping("/visitorIndex.jsp")
    public String index(Model model) {
        String visitorName = "游客";
        model.addAttribute("nickName", visitorName);
        return "/visitor/visitorIndex";
    }

}
