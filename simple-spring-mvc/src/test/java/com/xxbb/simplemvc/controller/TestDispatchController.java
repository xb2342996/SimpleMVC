package com.xxbb.simplemvc.controller;

import com.xxbb.simplemvc.annotation.RequestMapping;
import com.xxbb.simplemvc.annotation.RequestParam;
import com.xxbb.simplemvc.http.RequestMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
@RequestMapping(path = "/test")
public class TestDispatchController {

    @RequestMapping(path = "/dispatch", method = RequestMethod.GET)
    public String dispatch(@RequestParam(name = "name") String name, Model model) {
        System.out.println("dispatch controller.dispatch: name => " + name);
        model.addAttribute("name", name);
        return "redirect:/192.168.1.1";
    }
}
