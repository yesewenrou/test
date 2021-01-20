package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util;

import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.constant.ResultCodeEnum;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;

/**
 * @author lijiafeng
 * @date 2019/6/27 19:18
 */
public class ResultUtil {

    private static Result success(String message) {
        Result result = new Result();
        result.setCode(ResultCodeEnum.SUCCESS.getCode());
        result.setMessage(message);
        result.setSuccess(true);
        return result;
    }

    public static <T> Result<T> success() {
        return success(ResultCodeEnum.SUCCESS.getMessage());
    }

    public static <T> Result<T> buildSuccessResultWithData(T data) {
        Result<T> result = new Result<>();
        result.setCode(ResultCodeEnum.SUCCESS.getCode());
        result.setMessage(ResultCodeEnum.SUCCESS.getMessage());
        result.setSuccess(true);
        result.setData(data);
        return result;
    }

    private static Result fail(String message) {
        Result result = new Result();
        result.setCode(ResultCodeEnum.FAIL.getCode());
        result.setMessage(message);
        result.setSuccess(false);
        return result;
    }

    public static Result buildResult(boolean success, String message) {
        return success ? success(message) : fail(message);
    }

    public static Result buildResultId(boolean success, String message, Long id) {
        Result res = success ? success(message) : fail(message);
        res.setData(id);
        return res;
    }

    public static Result paramError(String errorMessage) {
        Result result = new Result();
        result.setCode(ResultCodeEnum.PARAM_ERROR.getCode());
        result.setMessage("参数检查不通过：" + errorMessage);
        result.setSuccess(false);
        return result;
    }

    public static Result paramError(BindingResult bindingResult) {
        List<ObjectError> allErrors = bindingResult.getAllErrors();
        ObjectError objectError = allErrors.get(0);
        String errorMessage = objectError.getDefaultMessage();
        return paramError(errorMessage);
    }

    public static Result noPermission() {
        Result result = new Result();
        result.setCode(ResultCodeEnum.NO_PERMISSION.getCode());
        result.setMessage(ResultCodeEnum.NO_PERMISSION.getMessage());
        result.setSuccess(false);
        return result;
    }

    public static Result fail(String code, String message){
        Result result = new Result();
        result.setSuccess(false);
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
}
