package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.config;

import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * @author lijiafeng
 * @date 2019/6/28 9:41
 */
@Configuration
public class ValidatorConfig {

    /**
     * 配置参数校验<br>
     * 检查到第一个不合法参数就返回
     *
     * @return validator
     */
    @Bean
    public Validator validator() {
        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
                .configure()
                .failFast(true)
                .buildValidatorFactory();
        return validatorFactory.getValidator();
    }
}
