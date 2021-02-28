package com.xxbb.simplemvc;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;

public class DispatchServletTest extends BaseJunit4Test{
    @Autowired
    private DispatcherServlet dispatcherServlet;

    @Test
    public void service() throws ServletException, IOException {
        dispatcherServlet.init();

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        request.setParameter("name", "xxbb");
        request.setRequestURI("/test/dispatch");

        dispatcherServlet.service(request, response);

        response.getHeaderNames().forEach(name -> {
            System.out.println(name + ": " + response.getHeader(name));
        });
    }

    @Test
    public void service2() throws ServletException, IOException {
        dispatcherServlet.init();

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        request.setParameter("name", "xxbb");
        request.setRequestURI("/test/dispatch2");

        dispatcherServlet.service(request, response);

        System.out.println(response.getContentAsString());
    }
}