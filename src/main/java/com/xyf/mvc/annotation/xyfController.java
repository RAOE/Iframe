package com.xyf.mvc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) // 当前的注解在哪个地方使用 作用范围
@Retention(RetentionPolicy.RUNTIME) // 运行时候通过反射来执行
@Documented // 当前的注解可以包含在javadoc 里面
public @interface xyfController {

	String value() default "";
}
