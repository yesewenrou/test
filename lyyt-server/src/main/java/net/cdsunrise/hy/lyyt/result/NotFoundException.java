package net.cdsunrise.hy.lyyt.result;

import net.cdsunrise.common.utility.vo.Result;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author suzhouhe  @date 2019/1/28 9:13  @describe : 404http未找到异常捕获
 */
@RestController
public class NotFoundException implements ErrorController {

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping(value = {"/error"})
    public Result error(HttpServletRequest request) {
        return new DataResult().failed("path error", "path error");
    }
}
