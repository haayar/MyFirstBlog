package com.wjj.blog.common.cache;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cache {
    long expire() default  1*60*1000;
    //key
    String name() default "";

}
