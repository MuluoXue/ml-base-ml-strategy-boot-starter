package com.ml.strategy.annotation;

import java.lang.annotation.*;

/**
 * @author ml
 * @date 2025年05月18日 22:31
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Strategy {

    /**
     * 策略类型标识（必须）
     * @return 策略类型标识
     */
    String type();

    /**
     * 策略所属分组（可选）
     * 用于区分不同的策略分组，默认值为空字符串
     * @return 策略所属分组
     */
    String group() default "";

    /**
     * 策略版本（可选）
     * 用于区分不同的策略版本，默认值为空字符串
     * @return 策略版本
     */
    String version() default "";
}