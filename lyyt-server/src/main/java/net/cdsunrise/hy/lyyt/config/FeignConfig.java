package net.cdsunrise.hy.lyyt.config;

import feign.RequestInterceptor;
import net.cdsunrise.hy.sso.starter.common.CustomContext;
import net.cdsunrise.hy.sso.starter.domain.TokenInfo;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author LHY
 * 解决内部feign调用时，token校验问题
 */
@Configuration
public class FeignConfig {

    @Bean
    @ConditionalOnMissingBean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            String token = null;
            TokenInfo tokenInfo = CustomContext.getTokenInfo();
            if (tokenInfo != null) {
                token = tokenInfo.getToken();
            }
            requestTemplate.header("token", token);
            requestTemplate.header("Request-Source", "inner");
        };
    }
}
