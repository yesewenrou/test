package net.cdsunrise.hy.lyyt.result;

import lombok.Data;
import net.cdsunrise.common.utility.utils.JsonUtils;
import net.cdsunrise.common.utility.vo.Result;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.annotation.PostConstruct;
import java.util.Objects;

/**
 * @author suzhouhe  @date 2018/12/18 16:48  @describe : 返回结果全局捕获
 */
@ControllerAdvice({"net.cdsunrise.hy.lyyt.controller"})
public class DataResultResponseBodyAdvice implements ResponseBodyAdvice {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @PostConstruct
    public void init(){
        System.out.println();
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (Objects.isNull(body)) {
            body = new DataResult<>().success();
        } else if (body instanceof String) {
            body = JsonUtils.toJsonString(new DataResult<>().success(body));
        } else if (!(body instanceof Result)) {
            body = new DataResult<>().success(body);
        }
        return body;
    }

    @Data
    private static class StringResult {
        private Object result;
    }
}
