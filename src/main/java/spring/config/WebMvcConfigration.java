package spring.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import spring.interceptor.MyInterceptor;

//拦截器
@Configuration
public class WebMvcConfigration implements WebMvcConfigurer {

    @Autowired
    private MyInterceptor myInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //registry.addInterceptor(myInterceptor).addPathPatterns("/**");
    }
}
