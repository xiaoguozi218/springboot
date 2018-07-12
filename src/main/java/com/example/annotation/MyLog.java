package com.example.annotation;

import java.lang.annotation.*;

/**
 * Created by MintQ on 2018/7/12.
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyLog {
    String value();
}
