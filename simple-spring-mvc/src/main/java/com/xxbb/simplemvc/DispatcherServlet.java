package com.xxbb.simplemvc;


import com.xxbb.simplemvc.handler.HandlerExecutionChain;
import com.xxbb.simplemvc.handler.mapping.HandlerMapping;
import com.xxbb.simplemvc.util.RequestContextHolder;
import com.xxbb.simplemvc.view.View;
import com.xxbb.simplemvc.view.resolver.ViewResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class DispatcherServlet extends HttpServlet implements ApplicationContextAware {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ApplicationContext applicationContext;
    private HandlerAdapter handlerAdapter;
    private HandlerMapping handlerMapping;
    private ViewResolver viewResolver;

    @Override
    public void init() {
        handlerMapping = this.applicationContext.getBean(HandlerMapping.class);
        handlerAdapter = this.applicationContext.getBean(HandlerAdapter.class);
        viewResolver = this.applicationContext.getBean(ViewResolver.class);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestContextHolder.setRequest(req);
        try {
            doDispatch(req, resp);
        } catch (Exception e) {
            logger.error("Handler the request fail", e);
        } finally {
            RequestContextHolder.resetRequest();
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Exception dispatchException = null;
        HandlerExecutionChain executionChain = null;
        try {
            ModelAndView mv = null;
            try {
                executionChain = this.handlerMapping.getHandler(request);
                if (!executionChain.applyPreHandle(request, response)) {
                    return;
                }
                mv = handlerAdapter.handle(request, response, executionChain.getHandler());
                executionChain.applyPostHandle(request, response, mv);
            } catch (Exception e) {
                dispatchException = e;
            }
            processDispatchResult(request, response, mv, dispatchException);
        } catch (Exception e) {
            dispatchException = e;
            throw e;
        } finally {
            if (Objects.nonNull(executionChain)) {
                executionChain.triggerAfterCompletion(request, response, dispatchException);
            }
        }
    }

    private void processDispatchResult(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView, Exception ex) throws Exception {
        if (Objects.nonNull(ex)) {
            modelAndView = processHandlerException(request, response, ex);
        }

        if (Objects.nonNull(modelAndView)) {
            render(modelAndView, request, response);
            return;
        }
        logger.info("No view rendering, null ModelAndView returned");
    }

    private ModelAndView processHandlerException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        return null;
    }

    private void render(ModelAndView mv, HttpServletRequest request, HttpServletResponse response) throws Exception {
        View view;
        String viewName = mv.getViewName();
        if (!StringUtils.isEmpty(viewName)) {
            view = viewResolver.resolveViewName(viewName);
        } else {
            view = (View) mv.getView();
        }
        if (mv.getStatus() != null) {
            response.setStatus(mv.getStatus().getValue());
        }
        view.render(mv.getModel().asMap(), request, response);
    }
}
