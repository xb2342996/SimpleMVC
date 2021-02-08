package com.xxbb.simplemvc.interceptor;

import com.xxbb.simplemvc.handler.interceptor.InterceptorRegistry;
import com.xxbb.simplemvc.handler.interceptor.MappedInterceptor;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class HandlerInterceptorTest {
    private final InterceptorRegistry interceptorRegistry = new InterceptorRegistry();

    @Test
    public void test() throws Exception {
        TestHandlerInterceptor interceptor = new TestHandlerInterceptor();

        interceptorRegistry.addInterceptor(interceptor).addExcludePatterns("/ex_test").addIncludePatterns("/in_test");
        List<MappedInterceptor> interceptorList = interceptorRegistry.getMappedInterceptors();
        Assert.assertEquals(interceptorList.size(), 1);

        MappedInterceptor mappedInterceptor = interceptorList.get(0);

        Assert.assertTrue(mappedInterceptor.matches("/in_test"));
        Assert.assertFalse(mappedInterceptor.matches("/ex_test"));

        mappedInterceptor.preHandler(null, null, null);
        mappedInterceptor.postHandler(null, null, null, null);
        mappedInterceptor.afterCompletion(null, null, null, null);
    }
}
