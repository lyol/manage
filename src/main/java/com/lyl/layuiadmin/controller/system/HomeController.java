package com.lyl.layuiadmin.controller.system;

import com.lyl.layuiadmin.service.system.HomePageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {
	@Autowired
    HomePageService homePageService;

    @GetMapping("/home/home")
    public String home() {
        return "home";
    }
   
}
