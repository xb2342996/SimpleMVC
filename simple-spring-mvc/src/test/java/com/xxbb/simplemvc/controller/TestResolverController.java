package com.xxbb.simplemvc.controller;

import com.xxbb.simplemvc.annotation.RequestBody;
import com.xxbb.simplemvc.annotation.RequestMapping;
import com.xxbb.simplemvc.annotation.RequestParam;
import com.xxbb.simplemvc.http.RequestMethod;
import com.xxbb.simplemvc.vo.UserVo;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TestResolverController {
    @RequestMapping(path = "/test4", method = RequestMethod.POST)
    public void test4(@RequestParam(name = "name") String name, @RequestParam(name = "age") String age,
                      @RequestParam(name = "birthday") Date date) {

    }

    @RequestMapping(path = "/user", method = RequestMethod.POST)
    public void user(@RequestBody UserVo vo) {

    }
}
