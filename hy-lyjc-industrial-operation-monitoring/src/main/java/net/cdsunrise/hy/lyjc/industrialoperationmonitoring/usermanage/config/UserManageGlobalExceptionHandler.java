package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.config;

import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.exception.BusinessException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author YQ
 */
@Slf4j
@ControllerAdvice(basePackages = {"net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage"})
@ResponseBody
public class UserManageGlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public Result exceptionHandler(Exception e) {
        log.error("Error in {}: {}", e.getClass().getSimpleName(), e);
        Result<Object> result = new Result<>();
        result.setSuccess(false);
        if (e instanceof BusinessException) {
            BusinessException e1 = (BusinessException) e;
            result.setCode(e1.getCode());
            result.setMessage(e1.getChineseMessage());
        } else {
            result.setMessage(e.getMessage());
        }
        return result;
    }
}
