package com.lyl.layuiadmin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication(scanBasePackages = "com.lyl.layuiadmin")
@EnableCaching
public class LayuiAdminApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(LayuiAdminApplication.class, args);
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseRegisteredSuffixPatternMatch(true);
    }

}
