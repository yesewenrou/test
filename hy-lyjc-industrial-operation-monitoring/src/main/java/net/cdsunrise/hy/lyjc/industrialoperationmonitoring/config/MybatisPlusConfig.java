package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.config;

import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author lijiafeng
 * @date 2019/6/27 17:14
 */
@Configuration
@EnableTransactionManagement
@MapperScan({"net.cdsunrise.hy.lyjc.industrialoperationmonitoring.mapper"})
public class MybatisPlusConfig {

    /**
     * 分页插件
     *
     * @return 分页拦截器
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    /**
     * 乐观锁插件
     *
     * @return 乐观锁拦截器
     */
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }
}
