package com.xxbb.simplemvc.controller;

import com.xxbb.simplemvc.annotation.ResponseBody;
import com.xxbb.simplemvc.handler.returnvalue.View;
import com.xxbb.simplemvc.vo.UserVo;
import org.springframework.ui.Model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TestReturnValueController {
    @ResponseBody
    public UserVo testResponseBody() {
        UserVo vo = new UserVo();
        vo.setAge("25");
        vo.setName("xxbb");
        vo.setBirthday(new Date());
        return vo;
    }

    public String testViewName() {
        return "/index/index.jsp";
    }

    public View testView() {
        return new View() {
        };
    }

    public Model testModel(Model model) {
        model.addAttribute("testModel", "model");
        return model;
    }

    public Map<String, Object> testMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("testMap", "map");
        return map;
    }
}
