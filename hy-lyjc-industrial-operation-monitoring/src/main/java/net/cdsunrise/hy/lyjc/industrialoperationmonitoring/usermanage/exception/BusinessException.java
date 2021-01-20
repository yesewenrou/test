package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.exception.ParamErrorException;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.enums.BusinessExceptionEnum;

import java.util.function.Supplier;

/**
 * @author YQ
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1622337383462701502L;
    private String code;
    private String message;
    private String chineseMessage;

    public BusinessException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public BusinessException(BusinessExceptionEnum exceptionEnum) {
        this.code = exceptionEnum.getCode();
        this.message = exceptionEnum.getMessage();
        this.chineseMessage = exceptionEnum.getChineseMessage();
    }

    public static Supplier<BusinessException> fail(BusinessExceptionEnum exceptionEnum){
        return () -> new BusinessException(exceptionEnum);
    }

    public static Supplier<ParamErrorException> toParamError(BusinessExceptionEnum exceptionEnum){
        return () -> new ParamErrorException(exceptionEnum.getChineseMessage());
    }
}
