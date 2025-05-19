package com.ml.strategy.processor;
import com.ml.strategy.annotation.Strategy;
import com.ml.strategy.config.StrategyProperties;
import com.ml.strategy.container.StrategyContainer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ml
 * @date 2025年05月18日 22:40
 */
@Component
public class StrategyBeanPostProcessor implements BeanPostProcessor {

    private final StrategyContainer container;
    private final StrategyProperties properties;

    public StrategyBeanPostProcessor(StrategyContainer container, StrategyProperties properties) {
        this.container = container;
        this.properties = properties;
    }

    @PostConstruct
    public void init() {
        System.out.println(">>>> requireDirectInterface = " + properties.isRequireDirectInterface());
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Strategy strategy = AnnotationUtils.findAnnotation(bean.getClass(), Strategy.class);
        if (strategy != null) {
            Class<?> clazz = bean.getClass();

            List<Class<?>> interfaces = null;
            if (properties.isRequireDirectInterface()) {
                interfaces = getAnyInterface(clazz);
                if (interfaces == null || interfaces.isEmpty()) {
                    throw new IllegalArgumentException("策略类 [" + clazz.getName() + "] 必须实现接口（可继承父类），否则不符合注册条件");
                }
            } else {
                // 必须直接实现接口
                interfaces = getDirectInterface(clazz);
                if (interfaces == null || interfaces.isEmpty()) {
                    throw new IllegalArgumentException("策略类 [" + clazz.getName() + "] 必须**直接实现接口**，或配置  ml.require-direct-interface=true");
                }
            }

            Class<?> strategyInterface = interfaces.get(0);
            if (strategyInterface == null) {
                throw new IllegalStateException("未能解析出注册接口类型，请确认策略类是否实现了接口");
            }

            String key = container.buildKey(strategy.type(), strategy.group(), strategy.version());
            container.register(strategyInterface, key, bean);
        }
        return bean;
    }

    /**
     * 获取实现接口， 排除spring的接口
     *
     * @param clazz 当前类
     * @return 接口列表
     */
    private List<Class<?>> getDirectInterface(Class<?> clazz) {
        Class<?>[] interfaces = clazz.getInterfaces();
        if (interfaces.length == 0) {
            return null;
        }
        return Arrays.stream(interfaces)
                .filter(interfaceClass -> !interfaceClass.getName().startsWith("org.springframework"))
                .collect(Collectors.toList());

    }

    /**
     * 检查类是否有接口（包括父类）
     *
     * @param clazz 当前类
     * @return true 如果有接口，否则 false
     */
    private List<Class<?>> getAnyInterface(Class<?> clazz) {
        while (clazz != null && clazz != Object.class) {
            List<Class<?>> interfaces = getDirectInterface(clazz);
            if (interfaces != null && !interfaces.isEmpty()) {
                return interfaces;
            }
            clazz = clazz.getSuperclass();
        }
        return null;
    }

}

