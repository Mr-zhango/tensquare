package cn.myfreecloud.tensquare.friend.config;

import cn.myfreecloud.tensquare.friend.intercept.JwtIntercept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
//WebMvcConfigurationSupport:用来配置springmvc的相关信息
public class InterceptConfig extends WebMvcConfigurationSupport {

    @Autowired
    private JwtIntercept jwtIntercept;

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtIntercept)   //设置使用哪个拦截器
                .addPathPatterns("/**")     //设置拦截的路径,这里拦截所有的请求
                .excludePathPatterns("/**/login");//设置哪些请求路径不会被拦截,登录不能被拦截
    }
}
