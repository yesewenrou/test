package net.cdsunrise.hy.lyyt.annatation;

import net.cdsunrise.hy.lyyt.enums.DataTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author YQ on 2019/11/15.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataType {
    DataTypeEnum[] value();
}
