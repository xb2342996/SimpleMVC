package com.xxbb.simplemvc.handler.returnvalue;

import com.xxbb.simplemvc.controller.TestReturnValueController;
import com.xxbb.simplemvc.handler.ModelAndViewContainer;
import com.xxbb.simplemvc.view.View;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.MethodParameter;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.ui.Model;

import java.lang.reflect.Method;

public class HandlerReturnValueHandlerTest {
    
    @Test
    public void test() throws Exception {
        HandlerMethodReturnValueHandlerComposite composite = new HandlerMethodReturnValueHandlerComposite();
        composite.addReturnValueHandler(new ModelMethodReturnValueHandler());
        composite.addReturnValueHandler(new ViewMethodReturnValueHandler());
        composite.addReturnValueHandler(new ViewNameMethodReturnValueHandler());
        composite.addReturnValueHandler(new ResponseBodyMethodReturnValueHandler());
        composite.addReturnValueHandler(new MapMethodReturnValueHandler());

        ModelAndViewContainer mvContainer = new ModelAndViewContainer();
        TestReturnValueController controller = new TestReturnValueController();

        Method viewNameMethod = controller.getClass().getMethod("testViewName");
        MethodParameter viewNameParameter = new MethodParameter(viewNameMethod, -1);
        composite.handleReturnValue(controller.testViewName(), viewNameParameter, mvContainer, null, null);
        Assert.assertEquals(mvContainer.getViewName(), "/index/index.jsp");

        Method viewMethod = controller.getClass().getMethod("testView");
        MethodParameter viewParameter = new MethodParameter(viewMethod, -1);
        composite.handleReturnValue(controller.testView(), viewParameter, mvContainer, null, null);
        Assert.assertTrue(mvContainer.getView() instanceof View);

        Method mapMethod = controller.getClass().getMethod("testMap");
        MethodParameter mapParameter = new MethodParameter(mapMethod, -1);
        composite.handleReturnValue(controller.testMap(), mapParameter, mvContainer, null, null);
        Assert.assertEquals(mvContainer.getModel().getAttribute("testMap"), "map");

        Method modelMethod = controller.getClass().getMethod("testModel", Model.class);
        MethodParameter modelParameter = new MethodParameter(modelMethod, -1);
        composite.handleReturnValue(controller.testModel(mvContainer.getModel()), modelParameter, mvContainer, null, null);
        Assert.assertEquals(mvContainer.getModel().getAttribute("testModel"), "model");

        Method bodyMethod = controller.getClass().getMethod("testResponseBody");
        MethodParameter bodyParameter = new MethodParameter(bodyMethod, -1);
        MockHttpServletResponse response = new MockHttpServletResponse();
        composite.handleReturnValue(controller.testResponseBody(), bodyParameter, mvContainer, null, response);
        System.out.println(response.getContentAsString());
    }
}
