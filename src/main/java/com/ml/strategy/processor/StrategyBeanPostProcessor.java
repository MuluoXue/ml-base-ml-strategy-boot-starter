package com.ml.strategy.processor;
import com.ml.strategy.annotation.Strategy;
import com.ml.strategy.container.StrategyContainer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

/**
 * @author ml
 * @date 2025年05月18日 22:40
 */
@Component
public class StrategyBeanPostProcessor implements BeanPostProcessor {

    private final StrategyContainer container;

    public StrategyBeanPostProcessor(StrategyContainer container) {
        this.container = container;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Strategy strategy = AnnotationUtils.findAnnotation(bean.getClass(), Strategy.class);
        if (strategy != null) {
            // 获取策略实现类的接口（支持多个接口时建议约定使用某个父接口或 marker 接口）
            Class<?>[] interfaces = bean.getClass().getInterfaces();
            if (interfaces.length == 0) {
                throw new IllegalArgumentException("Strategy class must implement at least one interface: " + bean.getClass());
            }

            // 默认取第一个接口（也可支持指定 interfaceClass 属性）
            Class<?> strategyInterface = interfaces[0];

            String key = container.buildKey(strategy.type(), strategy.group(), strategy.version());
            container.register(strategyInterface, key, bean);
        }
        return bean;
    }
}

