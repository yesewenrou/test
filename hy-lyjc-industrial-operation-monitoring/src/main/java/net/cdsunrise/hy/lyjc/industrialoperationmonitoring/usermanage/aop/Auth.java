package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.aop;

import java.lang.annotation.*;

/**
 * @author LHY
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Auth {

    String value() default "";

}
