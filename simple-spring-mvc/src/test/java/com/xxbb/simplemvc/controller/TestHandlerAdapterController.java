package com.xxbb.simplemvc.controller;

import org.springframework.ui.Model;

public class TestHandlerAdapterController {
    public String testViewName(Model model) {
        model.addAttribute("host", "127.0.0.1");
        return "/static/index.jsp";
    }
}
