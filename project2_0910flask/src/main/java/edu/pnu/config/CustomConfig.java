package edu.pnu.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class CustomConfig implements WebMvcConfigurer {
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// TODO Auto-generated method stub

		registry.addResourceHandler("/image/**")
				.addResourceLocations("classpath:/static/image/","file:/C:/uploads/","file:/C:/uploads/number/","file:/C:/0910/in","file:/C:/0910/out","file:/C:/uploads/board/","file:/C:\\Users\\user\\Desktop");
	}
}
