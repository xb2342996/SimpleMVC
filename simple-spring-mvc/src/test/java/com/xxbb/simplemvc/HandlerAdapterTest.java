package com.xxbb.simplemvc;

import com.google.gson.Gson;
import com.xxbb.simplemvc.controller.TestHandlerAdapterController;
import com.xxbb.simplemvc.handler.HandlerMethod;
import com.xxbb.simplemvc.handler.RequestMappingHandlerAdapter;
import org.junit.Test;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.ui.Model;

import java.lang.reflect.Method;

public class HandlerAdapterTest {
    @Test
    public void test1() throws Exception {
        TestHandlerAdapterController controller = new TestHandlerAdapterController();

        Method method = controller.getClass().getMethod("testViewName", Model.class);
        HandlerMethod handlerMethod = new HandlerMethod(controller, method);

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
        DateFormatter dateFormatter = new DateFormatter();
        dateFormatter.setPattern("yyyy-MM-dd HH:mm:ss");

        RequestMappingHandlerAdapter adapter = new RequestMappingHandlerAdapter();
        adapter.setConversionService(conversionService);
        adapter.afterPropertiesSet();

        ModelAndView modelAndView = adapter.handle(request, response, handlerMethod);

        System.out.println(new Gson().toJson(modelAndView));
    }
}
