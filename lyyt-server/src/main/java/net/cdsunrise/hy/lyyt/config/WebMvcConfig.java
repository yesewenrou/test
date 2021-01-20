package net.cdsunrise.hy.lyyt.config;

import net.cdsunrise.hy.sso.starter.interceptors.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author : suzhouhe  @date : 2019/7/25 9:50  @description : 拦截器配置
 */
//@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final TransferInterceptor transferInterceptor;

    private final TokenInterceptor tokenInterceptor;

    @Autowired
    public WebMvcConfig(TransferInterceptor transferInterceptor, TokenInterceptor tokenInterceptor) {
        this.transferInterceptor = transferInterceptor;
        this.tokenInterceptor = tokenInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor).addPathPatterns("/**").excludePathPatterns("/error");
        registry.addInterceptor(transferInterceptor).addPathPatterns("/**");
    }
}
