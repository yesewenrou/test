package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.config;

import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.exception.DictionaryException;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.exception.OrderByColumnNotSupportException;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.exception.ParamErrorException;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.exception.BusinessException;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.ResultUtil;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author lijiafeng
 * @date 2019/6/27 18:52
 */
@Slf4j
@RestControllerAdvice(basePackages = {"net.cdsunrise.hy.lyjc.industrialoperationmonitoring.controller"})
public class ControllerAdvise {

    /**
     * 请求内容格式或数据类型错误
     *
     * @return 处理结果
     */
    @ExceptionHandler({HttpMessageNotReadableException.class, HttpMediaTypeNotSupportedException.class})
    public Result httpMessageNotReadableException(Exception e) {
        log.error("请求内容不正确：{}", e.getMessage());
        return ResultUtil.buildResult(false, "请求内容格式或数据类型错误");
    }

    /**
     * 权限检查不通过
     *
     * @return 处理结果
     */
    @ExceptionHandler({BusinessException.class})
    public Result businessException(BusinessException e) {
        log.error(e.getMessage());
        return ResultUtil.noPermission();
    }

    /**
     * 参数检查不通过
     *
     * @return 处理结果
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result methodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        Result result = ResultUtil.paramError(bindingResult);
        log.error("参数检查不通过：{}", result.getMessage());
        return result;
    }

    /**
     * 参数检查不通过
     *
     * @return 处理结果
     */
    @ExceptionHandler(BindException.class)
    public Result bindException(BindException e) {
        BindingResult bindingResult = e.getBindingResult();
        Result result = ResultUtil.paramError(bindingResult);
        log.error("参数检查不通过：{}", result.getMessage());
        return result;
    }

    /**
     * 请求方式不支持
     *
     * @return 处理结果
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("请求方式不支持：{}", e.getMethod());
        return ResultUtil.buildResult(false, "请求方式不正确");
    }

    /**
     * 不支持的排序字段
     *
     * @return 处理结果
     */
    @ExceptionHandler(OrderByColumnNotSupportException.class)
    public Result orderByColumnNotSupportException(OrderByColumnNotSupportException e) {
        log.error(e.getMessage());
        return ResultUtil.paramError(e.getMessage());
    }

    /**
     * 参数错误异常
     *
     * @param e 参数错误异常
     * @return 处理结果
     */
    @ExceptionHandler(ParamErrorException.class)
    public Result paramErrorException(ParamErrorException e) {
        log.error("参数检查不通过：{}", e.getMessage());
        return ResultUtil.paramError(e.getMessage());
    }

    /**
     * 数据字典参数异常
     * @param e 异常
     * @return failed result
     */
    @ExceptionHandler(DictionaryException.class)
    public Result dictionaryException(DictionaryException e){
        log.error("dictionary error", e);
        return ResultUtil.fail(e.getCode(), e.getMessage());
    }


    /**
     * 未知错误
     *
     * @return 处理结果
     */
    @ExceptionHandler(Exception.class)
    public Result exception(Exception e) {
        log.error("未知错误", e);
        return ResultUtil.buildResult(false, "未知错误");
    }
}
