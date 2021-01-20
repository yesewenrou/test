package net.cdsunrise.hy.lyyt.init;

import net.cdsunrise.common.utility.AssertUtil;
import net.cdsunrise.hy.lyyt.enums.DataTypeEnum;
import net.cdsunrise.hy.lyyt.service.TransferService;
import net.cdsunrise.hy.lyyt.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @ClassName UtilInit
 * @Description
 * @Author LiuYin
 * @Date 2020/1/7 17:03
 */
@Component
@Order(1)
public class UtilInit implements ApplicationRunner {


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private TransferService transferService;

    /**
     * Callback used to run the bean.
     *
     * @param args incoming application arguments
     * @throws Exception on error
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
       initRedisUtil();
    }

    /**
     * 初始化Redis工具类
     */
    private void initRedisUtil(){
        AssertUtil.notNull(stringRedisTemplate, () -> new RuntimeException("string redis template is null at util init"));
        RedisUtil.setTemplateOnce(stringRedisTemplate);
    }

}
