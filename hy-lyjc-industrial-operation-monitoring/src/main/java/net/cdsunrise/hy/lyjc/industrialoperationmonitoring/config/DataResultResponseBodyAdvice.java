package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.config;

import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.utils.ResultUtil;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Objects;

/**
 * @Author: LHY
 * @Date: 2019/9/20 17:16
 */
@ControllerAdvice
public class DataResultResponseBodyAdvice implements ResponseBodyAdvice {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (Objects.isNull(body)) {
            body = ResultUtil.success();
        } else if (!(body instanceof byte[]) && !(body instanceof Result)) {
            body = ResultUtil.success(body);
        }
        return body;
    }
}
