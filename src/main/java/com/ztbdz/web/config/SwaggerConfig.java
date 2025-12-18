package com.ztbdz.web.config;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUI
public class SwaggerConfig {
    @Bean
    public Docket member() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("人员管理")
                .apiInfo(apiInfo())
                .select()
                .apis(Predicates.or(RequestHandlerSelectors.basePackage("com.ztbdz.user.controller"),RequestHandlerSelectors.basePackage("com.ztbdz.web.controller")))
                .paths(PathSelectors.any())
                .build();
    }
    @Bean
    public Docket tenderingBidding() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("招标投标")
                .apiInfo(apiInfo())
                .select()
                .apis(Predicates.or(RequestHandlerSelectors.basePackage("com.ztbdz.tenderee.controller"),RequestHandlerSelectors.basePackage("com.ztbdz.file.controller")))
                .paths(PathSelectors.any())
                .build();
    }


    private ApiInfo apiInfo(){
        return new ApiInfoBuilder().build();
    }
}
