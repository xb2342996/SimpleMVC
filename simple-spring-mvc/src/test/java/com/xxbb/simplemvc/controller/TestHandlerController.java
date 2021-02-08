package com.xxbb.simplemvc.controller;

import com.xxbb.simplemvc.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
public class TestHandlerController {

    @RequestMapping(path = "/ex_test")
    public void exTest() {

    }

    @RequestMapping(path = "/in_test1")
    public void inTest() {

    }
    @RequestMapping(path = "/in_test2")
    public void inTest2() {

    }
    @RequestMapping(path = "/in_test3")
    public void inTest3() {

    }
}
