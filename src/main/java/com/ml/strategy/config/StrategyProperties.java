package com.ml.strategy.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * @author ml
 * @date 2025年05月19日 00:22
 */
@ConfigurationProperties(prefix = "ml.strategy")
public class StrategyProperties {

    /**
     * 是否允许策略类继承实现接口，默认 false（不允许）
     */
    private boolean requireDirectInterface = false;

    public boolean isRequireDirectInterface() {
        return requireDirectInterface;
    }

    public void setRequireDirectInterface(boolean requireDirectInterface) {
        this.requireDirectInterface = requireDirectInterface;
    }
}
