package at.fhtw.mrp.controller;

import at.fhtw.restserver.http.Method;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Controller {
    String path() default "/api";
    Method method() default Method.GET;
}
