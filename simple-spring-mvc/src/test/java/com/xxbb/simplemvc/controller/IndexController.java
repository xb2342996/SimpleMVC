package com.xxbb.simplemvc.controller;

import com.xxbb.simplemvc.annotation.RequestMapping;
import com.xxbb.simplemvc.http.RequestMethod;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping(path = "/index")
public class IndexController {

    @RequestMapping(path = "/test", method = RequestMethod.GET)
    public void test() {

    }

    @RequestMapping(path = "/test2", method = RequestMethod.POST)
    public void test2(String name) {

    }

    public void test3(String name) {

    }
}
