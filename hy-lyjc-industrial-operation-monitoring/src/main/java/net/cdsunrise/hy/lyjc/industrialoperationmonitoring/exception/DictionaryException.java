package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.exception;

import lombok.Data;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.exception.enums.DictionaryExceptionEnum;

import java.util.function.Supplier;

/**
 * DictionaryException
 *
 * @author LiuYin
 * @date 2020/3/25 15:14
 */
@Data
public class DictionaryException extends RuntimeException{

    /**
     * 业务异常码
     */
    private String code;

    /**
     * 业务异常文字描述
     */
    private String message;

    public DictionaryException(DictionaryExceptionEnum exceptionEnum) {
        this.code = exceptionEnum.getCode();
        this.message = exceptionEnum.getMsg();
    }

    public static Supplier<DictionaryException> fail(DictionaryExceptionEnum exceptionEnum){
        return () -> new DictionaryException(exceptionEnum);
    }
}
