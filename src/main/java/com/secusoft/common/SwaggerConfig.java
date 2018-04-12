package com.secusoft.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger配置
 * @author yaojiacheng
 * 2018年4月12日
 */
@Configuration
@EnableSwagger2
@Profile({"dev","test"})
public class SwaggerConfig {
	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
				.apis(RequestHandlerSelectors.basePackage("com.secusoft.controller")).paths(PathSelectors.any()).build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("websetup接口测试")
				.description("接口返回采用统一格式，success为true表明处理成功，false表明失败；code为200表明处理成功，"+
						"500表明系统出现异常错误，400表明身份认证失败需重新登录，403表明没有访问权限；errorMsg是当接口处理失败时返回的错误信息；data是处理成功后的返回内容。")
				.termsOfServiceUrl("git.lidehang.work").contact("姚嘉诚").version("1.0").build();
	}
}
