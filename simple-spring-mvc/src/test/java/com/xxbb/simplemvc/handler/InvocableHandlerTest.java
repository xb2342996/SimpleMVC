package com.xxbb.simplemvc.handler;

import com.google.gson.Gson;
import com.xxbb.simplemvc.controller.TestInvocableHandlerController;
import com.xxbb.simplemvc.handler.argument.HandlerMethodArgumentResolverComposite;
import com.xxbb.simplemvc.handler.argument.ModelMethodArgumentResolver;
import com.xxbb.simplemvc.handler.argument.ServletRequestMethodArgumentResolver;
import com.xxbb.simplemvc.handler.argument.ServletResponseMethodArgumentResolver;
import com.xxbb.simplemvc.handler.returnvalue.HandlerMethodReturnValueHandlerComposite;
import com.xxbb.simplemvc.handler.returnvalue.ViewNameMethodReturnValueHandler;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class InvocableHandlerTest {
    @Test
    public void test1() throws Exception {
        TestInvocableHandlerController controller = new TestInvocableHandlerController();

        Method method = controller.getClass().getMethod("testRequestAndResponse", HttpServletRequest.class, HttpServletResponse.class);

        HandlerMethod handlerMethod = new HandlerMethod(controller, method);
        HandlerMethodArgumentResolverComposite argumentResolver = new HandlerMethodArgumentResolverComposite();
        argumentResolver.addResolver(new ServletRequestMethodArgumentResolver());
        argumentResolver.addResolver(new ServletResponseMethodArgumentResolver());

        InvocableHandlerMethod invocableMethod = new InvocableHandlerMethod(handlerMethod, argumentResolver, null, null);
        ModelAndViewContainer mavContainer = new ModelAndViewContainer();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("name", "xxbb");

        MockHttpServletResponse response = new MockHttpServletResponse();

        invocableMethod.invokeAndHandle(request, response, mavContainer);
        System.out.println(response.getContentAsString());
    }

    @Test
    public void test2() throws Exception {
        TestInvocableHandlerController controller = new TestInvocableHandlerController();
        Method method = controller.getClass().getMethod("testViewName", Model.class);

        HandlerMethod handlerMethod = new HandlerMethod(controller, method);
        HandlerMethodArgumentResolverComposite composite = new HandlerMethodArgumentResolverComposite();
        composite.addResolver(new ModelMethodArgumentResolver());

        HandlerMethodReturnValueHandlerComposite returnValueComposite = new HandlerMethodReturnValueHandlerComposite();
        returnValueComposite.addReturnValueHandler(new ViewNameMethodReturnValueHandler());

        InvocableHandlerMethod invocableHandlerMethod = new InvocableHandlerMethod(handlerMethod, composite, returnValueComposite, null);

        ModelAndViewContainer mavContainer = new ModelAndViewContainer();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        invocableHandlerMethod.invokeAndHandle(request, response, mavContainer);

        System.out.println(new Gson().toJson(mavContainer.getModel()));
        System.out.println(mavContainer.getViewName());
    }
}
