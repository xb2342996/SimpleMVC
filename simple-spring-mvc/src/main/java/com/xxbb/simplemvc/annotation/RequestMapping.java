package com.xxbb.simplemvc.annotation;

import com.xxbb.simplemvc.http.RequestMethod;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {
    String path();

    RequestMethod method() default RequestMethod.GET;
}
