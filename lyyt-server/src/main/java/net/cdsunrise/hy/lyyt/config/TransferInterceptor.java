package net.cdsunrise.hy.lyyt.config;

import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.hy.lyyt.annatation.DataType;
import net.cdsunrise.hy.lyyt.enums.DataTypeEnum;
import net.cdsunrise.hy.lyyt.helper.RunHelper;
import net.cdsunrise.hy.lyyt.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 调用统计拦截器
 *
 * @author YQ on 2019/11/15.
 */
@Component
@Slf4j
public class TransferInterceptor implements HandlerInterceptor {
    private final TransferService transferService;

    @Autowired
    public TransferInterceptor(TransferService transferService) {
        this.transferService = transferService;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            DataType dataType = handlerMethod.getMethodAnnotation(DataType.class);
            if (dataType != null) {
                final List<String> collect = Arrays.stream(dataType.value()).map(DataTypeEnum::name).collect(Collectors.toList());
                RunHelper.add(() -> transferService.normalTransfer(Stream.of(dataType.value()).collect(Collectors.toSet())), "transferService.normalTransfer:" + String.join(",",collect));
            }
        }
    }
}
