package com.xxbb.simplemvc.controller;

import com.xxbb.simplemvc.annotation.*;
import com.xxbb.simplemvc.exception.TestException;
import com.xxbb.simplemvc.http.RequestMethod;
import com.xxbb.simplemvc.vo.ApiR;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
@ControllerAdvice
@RequestMapping(path = "/test")
public class TestDispatchController {

    @RequestMapping(path = "/dispatch", method = RequestMethod.GET)
    public String dispatch(@RequestParam(name = "name") String name, Model model) {
        System.out.println("dispatch controller.dispatch: name => " + name);
        model.addAttribute("name", name);
        return "redirect:/192.168.1.1";
    }

    @RequestMapping(path = "/dispatch2", method = RequestMethod.GET)
    public String dispatch2(@RequestParam(name = "name") String name, Model model) {
        System.out.println("dispatch controller.dispatch: name => " + name);
        throw new TestException("test exception", name);
    }

    @ResponseBody
    @ExceptionHandler({TestException.class})
    public ApiR exceptionHandler(TestException ex) {
        System.out.println("exception message: " + ex.getMessage());
        return new ApiR(200, "exception finished", ex.getName());
    }
}
