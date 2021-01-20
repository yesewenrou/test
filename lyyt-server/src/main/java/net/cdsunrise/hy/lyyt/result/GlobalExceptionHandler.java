package net.cdsunrise.hy.lyyt.result;

import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyyt.exception.business.BusinessException;
import net.cdsunrise.hy.lyyt.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author suzhouhe  @date 2018/12/18 16:50  @describe : 异常全局捕获
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private final TransferService transferService;

    @Autowired
    public GlobalExceptionHandler(TransferService transferService) {
        this.transferService = transferService;
    }

    @ExceptionHandler(value = {Exception.class})
    public Result defaultErrorHandler(Exception e) {
        log.error("error", e);
        transferService.errorTransfer();
        if (e instanceof BusinessException) {
            BusinessException businessException = (BusinessException) e;
            return new DataResult().failed(businessException.getCode(), businessException.getMessage());
        }
        return new DataResult().internalError();
    }

}
