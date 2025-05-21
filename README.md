# 简介
ml-strategy-spring-boot-starter是一个基于 Spring Boot 的轻量级策略模式扩展组件。
它通过自定义注解和自动注册机制，实现多维度（如类型、分组、版本）策略类的灵活管理与动态调用，极大提升了多业务、多版本、多租户项目中接口实现的可扩展性和维护性，同时保证高性能和线程安全。
适合企业级项目快速构建可插拔、可扩展的服务架构

# 什么是策略模式？
策略模式是一种行为型设计模式，它定义了一系列的算法，把它们一个个封装起来，并且使它们可以互相替换。
通俗讲，就是“不同情况用不同处理逻辑”，通过接口统一管理，不改变原有代码即可添加新的逻辑策略。

# 适用场景
多个子业务共用同一套接口规范，但实际实现逻辑却因业务类型、租户、版本等维度而异，保证代码清晰、扩展性强和性能可控的同时，实现这类需求。 如：

● 多租户项目中，不同租户策略不同（如字段配置）

● 多版本接口共存（如 layoutV1 / layoutV2）

● 多模块服务调用（如 layout/field/page）策略组合

● 配置驱动行为切换（动态配置使用哪个实现）

# 设计目标
● ✅ 高扩展性：通过注解标记策略类，按需注册

● ✅ 自动注册：基于 Spring 的 BeanPostProcessor 自动收集

● ✅ 支持多维度匹配：如 type、group、version 等

● ✅ 线程安全：使用 ConcurrentHashMap 保证并发环境安全

● ✅ 轻量高性能：注册逻辑简单，启动性能基本无影响

# 优点
● 解耦强: 使用接口调用策略类，新增不影响旧逻辑

● 支持扩展:	支持 group / version 多维度扩展策略

● 自动注册:	不需要手动维护 Map 或工厂类

● 线程安全:	并发环境注册与调用均安全

● 启动快:	注册仅遍历 Spring Bean，启动基本无负担


# 使用示例：
如： 假设你有一个导出服务接口：ExportService
不同策略类实现它，如：ExcelExportService, PdfExportService。你希望运行时根据用户传入的格式来选择对应导出策略

```java
// 声明导出接口
public interface ExportService {
    void export(String data);
}
```


```java
// excel导出实现接口
@Strategy(type = "excel", group = "export", version = "v1")
public class ExcelExportService implements ExportService {

    @Override
    public void export(String data) {
        System.out.println("导出为 Excel：" + data);
    }
}
```

```java
// pdf导出实现接口
@Strategy(type = "pdf", group = "export", version = "v1")
public class PdfExportService implements ExportService {

    @Override
    public void export(String data) {
        System.out.println("导出为 PDF：" + data);
    }
}
```

```java
// 接口调用
@RestController
@RequestMapping("/export")
public class ExportController {

    private final StrategyContainer strategyContainer;

    public ExportController(StrategyContainer strategyContainer) {
        this.strategyContainer = strategyContainer;
    }

    @GetMapping
    public String export(@RequestParam String format, @RequestParam String data) {
        try {
            // 构建策略 key
            String key = strategyContainer.buildKey(format, "export", "v1");

            // 获取对应导出策略
            ExportService exportService = strategyContainer.getStrategy(ExportService.class, key);

            // 调用导出逻辑
            exportService.export(data);
            return "导出成功（格式：" + format + "）";
        } catch (Exception e) {
            return "导出失败：" + e.getMessage();
        }
    }
}
```

# 联系我
![image](https://github.com/user-attachments/assets/d03dedfb-b8e9-4b1b-91df-041701292c18)


