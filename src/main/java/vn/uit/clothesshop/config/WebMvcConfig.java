package vn.uit.clothesshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {
    @Bean
    @NonNull
    ViewResolver viewResolver() {
        final var bean = new InternalResourceViewResolver();
        bean.setViewClass(JstlView.class);
        bean.setPrefix("/WEB-INF/views/");
        bean.setSuffix(".jspx");
        return bean;
    }

    @Override
    public void configureViewResolvers(@NonNull final ViewResolverRegistry registry) {
        registry.viewResolver(viewResolver());
    }

    @Override
    public void addResourceHandlers(@NonNull final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/admin/**").addResourceLocations("/resources/admin/");
        registry.addResourceHandler("/client/**").addResourceLocations("/resources/client/");
        registry.addResourceHandler("/img/**").addResourceLocations("/resources/img/");
    }
}
