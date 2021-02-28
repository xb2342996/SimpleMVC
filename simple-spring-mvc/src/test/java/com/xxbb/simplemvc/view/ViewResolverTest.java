package com.xxbb.simplemvc.view;

import com.xxbb.simplemvc.util.RequestContextHolder;
import com.xxbb.simplemvc.view.resolver.ContentNegotiatingViewResolver;
import com.xxbb.simplemvc.view.resolver.InternalResourceViewResolver;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Collections;

public class ViewResolverTest {
    @Test
    public void testResolveName() throws Exception {
        ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
        resolver.setViewResolvers(Collections.singletonList(new InternalResourceViewResolver()));

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Accept", "text/html");
        RequestContextHolder.setRequest(request);

        View redirectView = resolver.resolveViewName("redirect:/127.0.0.1");
        Assert.assertTrue(redirectView instanceof RedirectView);

        View forwardView = resolver.resolveViewName("forward:/127.0,0.1");
        Assert.assertTrue(forwardView instanceof InternalResourceView);

        View view = resolver.resolveViewName("/127.0.0.1");
        Assert.assertTrue(view instanceof InternalResourceView);
    }
}
