package com.ml.strategy.container;
import org.springframework.stereotype.Component;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 策略注册器
 * @author ml
 * @date 2025年05月18日 22:32
 */
@Component
public class StrategyContainer {

    // 多级Map：接口类型 -> 策略Key -> 策略实例
    private final ConcurrentHashMap<Class<?>, ConcurrentHashMap<String, Object>> strategyMap = new ConcurrentHashMap<>();

    public void register(Class<?> strategyType, String key, Object bean) {
        strategyMap
                .computeIfAbsent(strategyType, k -> new ConcurrentHashMap<>())
                .putIfAbsent(key, bean);
    }

    public <T> T getStrategy(Class<T> strategyType, String key) {
        ConcurrentHashMap<String, Object> map = strategyMap.get(strategyType);
        if (map == null || !map.containsKey(key)) {
            throw new IllegalArgumentException("No strategy found for type: " + strategyType.getSimpleName() + ", key: " + key);
        }
        return strategyType.cast(map.get(key));
    }

    public String buildKey(String type) {
        return buildKey(type, "", "");
    }

    public String buildKey(String type, String group, String version) {
        return String.format("%s:%s:%s", type, group, version);
    }
}

