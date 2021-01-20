package net.cdsunrise.hy.lyyt.result;

import lombok.Data;
import net.cdsunrise.common.utility.vo.Result;

/**
 * @author suzhouhe  @date 2018/12/18 15:48  @describe : 返回结果实体封装
 */
@Data
class DataResult<T> {

    private static final String SUCCESS_CODE = "00000000";

    private static final String SUCCESS_MSG = "处理成功";

    private static final String FAILED_CODE = "00009999";

    private static final String FAILED_MSG = "内部错误";

    Result success() {
        Result result = new Result();
        result.setCode(SUCCESS_CODE);
        result.setMessage(SUCCESS_MSG);
        result.setSuccess(true);
        return result;
    }

    Result<T> success(T data) {
        Result<T> result = this.success();
        result.setData(data);
        return result;
    }

    Result failed(String code, String message) {
        Result result = new Result();
        result.setCode(code);
        result.setMessage(message);
        result.setSuccess(false);
        return result;
    }

    Result internalError() {
        return this.failed(FAILED_CODE, FAILED_MSG);
    }
}
