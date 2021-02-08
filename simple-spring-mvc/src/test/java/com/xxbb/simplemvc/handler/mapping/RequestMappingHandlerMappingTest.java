package com.xxbb.simplemvc.handler.mapping;

import com.xxbb.simplemvc.BaseJunit4Test;
import com.xxbb.simplemvc.controller.TestHandlerController;
import com.xxbb.simplemvc.exception.NoHandlerFoundException;
import com.xxbb.simplemvc.handler.HandlerExecutionChain;
import com.xxbb.simplemvc.handler.HandlerMethod;
import com.xxbb.simplemvc.handler.interceptor.MappedInterceptor;
import com.xxbb.simplemvc.http.RequestMethod;
import com.xxbb.simplemvc.interceptor.Test2HandlerInterceptor;
import com.xxbb.simplemvc.interceptor.TestHandlerInterceptor;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;

public class RequestMappingHandlerMappingTest extends BaseJunit4Test {
    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    @Autowired
    private RequestMappingHandlerMapping interceptorHandlerMapping;

    @Test
    public void test() {
        MappingRegistry mappingRegistry = handlerMapping.getMappingRegistry();

        String path = "/index/test";
        String path2 = "/index/test2";
        String path4 = "/test4";

        Assert.assertEquals(mappingRegistry.getPathHanderMethod().size(), 2);

        HandlerMethod method = mappingRegistry.getHandlerMethodByPath(path);
        HandlerMethod method2 = mappingRegistry.getHandlerMethodByPath(path2);
        HandlerMethod method4 = mappingRegistry.getHandlerMethodByPath(path4);

        Assert.assertNull(method4);
        Assert.assertNotNull(method);
        Assert.assertNotNull(method2);

        RequestMappingInfo info = mappingRegistry.getMappingByPath(path);
        RequestMappingInfo info2 = mappingRegistry.getMappingByPath(path2);

        Assert.assertNotNull(info);
        Assert.assertNotNull(info2);

        Assert.assertNotNull(info);
        Assert.assertNotNull(info2);
        Assert.assertEquals(info.getHttpMethod(), RequestMethod.GET);
        Assert.assertEquals(info2.getHttpMethod(), RequestMethod.POST);
    }

    @Test
    public void getHandlerTest() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/in_test1");
        HandlerExecutionChain executionChain = interceptorHandlerMapping.getHandler(request);

        HandlerMethod handlerMethod = executionChain.getHandler();
        Assert.assertTrue(handlerMethod.getBean() instanceof TestHandlerController);
        Assert.assertTrue(((MappedInterceptor) executionChain.getInterceptors().get(0))
                .getInterceptor() instanceof TestHandlerInterceptor);

        request.setRequestURI("/ex_test");
        executionChain = interceptorHandlerMapping.getHandler(request);
        Assert.assertEquals(executionChain.getInterceptors().size(), 0);

        request.setRequestURI("/in_test1234");
        Assert.assertThrows(NoHandlerFoundException.class, () ->  interceptorHandlerMapping.getHandler(request));

        request.setRequestURI("/in_test2");
        executionChain = interceptorHandlerMapping.getHandler(request);
        Assert.assertEquals(executionChain.getInterceptors().size(), 1);
        Assert.assertTrue(((MappedInterceptor) executionChain.getInterceptors().get(0)).getInterceptor() instanceof Test2HandlerInterceptor);

        request.setRequestURI("/in_test3");
        executionChain = interceptorHandlerMapping.getHandler(request);
        Assert.assertEquals(executionChain.getInterceptors().size(), 1);
        Assert.assertTrue(((MappedInterceptor) executionChain.getInterceptors().get(0)).getInterceptor() instanceof Test2HandlerInterceptor);

    }
}
