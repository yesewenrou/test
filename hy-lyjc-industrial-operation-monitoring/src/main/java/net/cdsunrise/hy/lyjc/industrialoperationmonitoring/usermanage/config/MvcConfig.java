package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.config;

import net.cdsunrise.hy.sso.starter.interceptors.TokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author LHY
 */
//@Configuration
public class MvcConfig implements WebMvcConfigurer {

    /**
     * 调用SSO拦截器
     */
    private TokenInterceptor tokenInterceptor;

    public MvcConfig(TokenInterceptor tokenInterceptor) {
        this.tokenInterceptor = tokenInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor).addPathPatterns("/**")
                .excludePathPatterns("/login/**")
                .excludePathPatterns("/complaintManage/add")
                .excludePathPatterns("/actuator/**")
                .excludePathPatterns("/um/user/findUserByMenuForWarningMessage")
                .excludePathPatterns("/autoWarning/trafficWarning")
                .excludePathPatterns("/dataCenterMonthlyReport/report")
//                .excludePathPatterns("/dataResource/tourist/region/statistics")
//                .excludePathPatterns("/dataResource/tourist/region/compare")
                .order(1);
    }
}
