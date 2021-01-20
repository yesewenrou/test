package net.cdsunrise.hy.lyyt.utils;

import java.util.Objects;

/**
 * NumberUtil
 *
 * @author LiuYin
 * @date 2020/1/17 12:42
 */
public class NumberUtil {

    public static Integer nullToZero(Integer i){
        return Objects.isNull(i)? 0 : i;
    }

    public static Long nullToZero(Long number){
        return Objects.isNull(number)? 0L : number;
    }

    public static Double nullToZero(Double number){
        return Objects.isNull(number) ? 0D : number;
    }
}
