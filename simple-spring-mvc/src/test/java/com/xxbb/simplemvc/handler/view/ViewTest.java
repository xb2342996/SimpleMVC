package com.xxbb.simplemvc.handler.view;

import com.xxbb.simplemvc.view.RedirectView;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.HashMap;
import java.util.Map;

public class ViewTest {
    @Test
    public void test() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContextPath("/path");

        MockHttpServletResponse response = new MockHttpServletResponse();

        Map<String, Object> model = new HashMap<>();
        model.put("name", "xxbb");
        model.put("url", "127.0.0.1");

        RedirectView view = new RedirectView("/redirect/login");
        view.render(model, request, response);

        response.getHeaderNames().forEach(name -> {
            System.out.println(name + ":" + response.getHeader(name));
        });
    }
}
