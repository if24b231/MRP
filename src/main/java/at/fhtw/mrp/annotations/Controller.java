package at.fhtw.mrp.annotations;

import at.fhtw.restserver.http.Method;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Controller {
    String path();
    Method method() default Method.GET;
    boolean authenticationNeeded() default true;
}
