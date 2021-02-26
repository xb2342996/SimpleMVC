package com.xxbb.simplemvc.controller;


import org.springframework.ui.Model;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class TestInvocableHandlerController {
    public void testRequestAndResponse(HttpServletRequest request, HttpServletResponse response) {
        Assert.notNull(request, "request cannot be null");
        Assert.notNull(response, "response cannot be null");

        try (PrintWriter writer = response.getWriter()) {
            String name = request.getParameter("name");
            writer.println("hellow invocableHandlerMethod, param: " + name);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String testViewName(Model model){
        model.addAttribute("host", "127.0.0.1");
        return "/static/index.jsp";
    }
}
