package com.xxbb.simplemvc.handler.argument;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xxbb.simplemvc.controller.TestResolverController;
import com.xxbb.simplemvc.handler.HandlerMethod;
import com.xxbb.simplemvc.vo.UserVo;
import org.junit.Test;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.lang.reflect.Method;
import java.util.Date;

public class HandlerArgumentResolverTest {
    
    @Test
    public void test() throws NoSuchMethodException {
        TestResolverController controller = new TestResolverController();
        Method method = controller.getClass().getMethod("test4", String.class, String.class, Date.class);

        HandlerMethod handlerMethod = new HandlerMethod(controller, method);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("name", "xxbb");
        request.setParameter("age", "28");
        request.setParameter("birthday", "2020-11-11 23:49:21");

        HandlerMethodArgumentResolverComposite composite = new HandlerMethodArgumentResolverComposite();
        composite.addResolver(new RequestParamMethodArgumentResolver());
        composite.addResolver(new ServletRequestMethodArgumentResolver());

        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
        DateFormatter dateFormatter = new DateFormatter();
        dateFormatter.setPattern("yyyy-MM-dd HH:mm:ss");
        conversionService.addFormatter(dateFormatter);

        MockHttpServletResponse response = new MockHttpServletResponse();
        DefaultParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
        handlerMethod.getParameters().forEach(methodParameter -> {
            try {
                methodParameter.initParameterNameDiscovery(parameterNameDiscoverer);
                Object value = composite.resolveArgument(methodParameter, request, response, null, conversionService);
                System.out.println(methodParameter.getParameter() + ": " + value + "   type: " + value.getClass());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void test2() throws Exception {
        TestResolverController controller = new TestResolverController();
        Method method = controller.getClass().getMethod("user", UserVo.class);

        HandlerMethod handlerMethod = new HandlerMethod(controller, method);

        MockHttpServletRequest request = new MockHttpServletRequest();
        UserVo userVo = UserVo.builder().name("xxbb").age("28").birthday(new Date()).build();
        ObjectMapper mapper = new ObjectMapper();
        request.setContent(mapper.writeValueAsString(userVo).getBytes());

        HandlerMethodArgumentResolverComposite composite = new HandlerMethodArgumentResolverComposite();
        composite.addResolver(new RequestBodyMethodArgumentResolver());

        MockHttpServletResponse response = new MockHttpServletResponse();
        DefaultParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
        handlerMethod.getParameters().forEach(methodParameter -> {
            try {
                methodParameter.initParameterNameDiscovery(parameterNameDiscoverer);
                Object value = composite.resolveArgument(methodParameter, request, response, null, null);
                System.out.println(methodParameter.getParameterName() + ": " + value + "   type: " + value.getClass());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
