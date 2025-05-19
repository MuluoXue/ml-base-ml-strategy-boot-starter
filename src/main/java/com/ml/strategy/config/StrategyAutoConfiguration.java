package com.ml.strategy.config;
import com.ml.strategy.container.StrategyContainer;
import com.ml.strategy.processor.StrategyBeanPostProcessor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ml
 * @date 2025年05月18日 22:34
 */
@Configuration
@EnableConfigurationProperties(StrategyProperties.class)
public class StrategyAutoConfiguration {

    @Bean
    public StrategyContainer strategyContainer() {
        return new StrategyContainer();
    }

    @Bean
    public StrategyBeanPostProcessor strategyBeanPostProcessor(StrategyContainer strategyContainer,
                                                               StrategyProperties properties) {
        return new StrategyBeanPostProcessor(strategyContainer, properties);
    }
}