package com.bawei.springbootfor1704e.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class HelloController {

    @RequestMapping("/hello")
    public String hello() {
        return "hello";
    }

    @RequestMapping("/FirstEcharts.html")
    public ModelAndView firstEcharts() {
        return new ModelAndView("FirstEcharts");
    }
}
