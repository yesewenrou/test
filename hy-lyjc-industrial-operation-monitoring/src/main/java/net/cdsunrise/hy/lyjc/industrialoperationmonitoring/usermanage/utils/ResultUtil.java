package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.utils;

import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.enums.BusinessExceptionEnum;

/**
 * @author YQ
 */
public class ResultUtil<T> {
    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> success(T data) {
        return build(BusinessExceptionEnum.SUCCESS.getCode(), BusinessExceptionEnum.SUCCESS.getMessage(), data, true);
    }

    public static <T> Result<T> success(String code, String msg, T data) {
        return build(code, msg, data, true);
    }

    public static <T> Result<T> fail() {
        return fail(BusinessExceptionEnum.FAILED.getCode(), BusinessExceptionEnum.FAILED.getMessage(), null);
    }

    public static <T> Result<T> fail(String code, String msg, T data) {
        return build(code, msg, data, false);
    }

    public static <T> Result<T> build(String code, String msg, T data, boolean success) {
        Result<T> result = new Result<>();
        result.setMessage(msg);
        result.setCode(code);
        result.setData(data);
        result.setSuccess(success);
        return result;
    }
}
