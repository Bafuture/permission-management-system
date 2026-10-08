package com.atheli.system.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * knife4j配置信息 (Spring Boot 3 + OpenAPI 3)
 */
@Configuration
public class Knife4jConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        List<Parameter> globalParameters = new ArrayList<>();

        // 添加 token header 参数
        Parameter tokenParam = new Parameter()
                .name("token")
                .description("用户token")
                .in("header")
                .required(false);

        globalParameters.add(tokenParam);

        return new OpenAPI()
                .info(new Info()
                        .title("后台管理系统-API文档")
                        .description("本文档描述了后台管理系统微服务接口定义")
                        .version("1.0")
                        .contact(new Contact()
                                .name("巴艺博")
                                .url("http://atheli.com")
                                .email("1308679336@qq.com")))
                .servers(List.of(
                        new Server()
                                .url("/admin/system/")
                                .description("默认服务器")
                ));
    }
}
