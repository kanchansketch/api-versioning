package com.example.demo.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiVersion {
    int[] value(); // multiple versions allowed
    /**
     * Strategy override for this specific mapping.
     * If DEFAULT, fall back to global config (application.yml).
     */
    Strategy strategy() default Strategy.DEFAULT;

    enum Strategy {
        DEFAULT,  // use global config
        PATH,     // /api/v1/...
        HEADER,   // X-API-Version: 1
        QUERY     // ?version=1
    }
}
