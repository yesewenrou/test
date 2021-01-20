package net.cdsunrise.hy.lyyt.exception.business;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.function.Supplier;

/**
 * @author suzhouhe  @date 2018/12/18 13:46  @describe ： 业务异常类
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BusinessException extends RuntimeException {

    /**
     * 业务异常码
     */
    private String code;

    /**
     * 业务异常文字描述
     */
    private String message;

    public BusinessException(BusinessExceptionEnum exceptionEnum) {
        this.code = exceptionEnum.getCode();
        this.message = exceptionEnum.getMsg();
    }

    public static Supplier<BusinessException> fail(BusinessExceptionEnum exceptionEnum){
        return () -> new BusinessException(exceptionEnum);
    }

}
