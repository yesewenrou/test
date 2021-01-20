package net.cdsunrise.hy.lyyt.config;

import com.hikvision.artemis.sdk.config.ArtemisConfig;
import net.cdsunrise.common.utility.AssertUtil;
import net.cdsunrise.hy.lyyt.entity.co.RoadSectionCo;
import net.cdsunrise.hy.lyyt.service.InitService;
import net.cdsunrise.hy.lyyt.utils.RoadSectionUtil;
import net.cdsunrise.hy.lyyt.utils.YdpInfoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: suzhouhe @Date: 2019/11/5 15:40 @Description:
 */
@Component
public class StartConfig implements ApplicationRunner {

    @Autowired
    private PropertiesConfig propertiesConfig;

    @Autowired
    private InitService initService;

    @Autowired
    private HyConfiguration hyConfiguration;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 初始化诱导屏工具
        initYdpUtil();
        // 初始化路段信息
        initRoadSection();
        // 初始化海康信息
        this.initHikvision();
        // 初始化大数据中心数据
        this.initBaseData();
    }

    private void initHikvision() {
        PropertiesConfig.Artemisconfig artemisconfig = propertiesConfig.getArtemisconfig();
        ArtemisConfig.host = artemisconfig.getHost() + ":" + artemisconfig.getPort();
        ArtemisConfig.appKey = artemisconfig.getAppKey();
        ArtemisConfig.appSecret = artemisconfig.getAppSecret();
    }

    /**
     * 初始化大数据中心基础数据
     */
    private void initBaseData(){
        initService.initDataType();
        initService.initDataSource();
        initService.initDataBaseType();
    }

    /**
     * 初始化道路信息
     */
    private void initRoadSection(){
        final List<RoadSectionCo> roadSections = hyConfiguration.getRoadSections();
        AssertUtil.notEmpty(roadSections, () -> new RuntimeException("road section list is empty on initRoadSectionMap"));

        // 设置redis模板
        RoadSectionUtil.setStringRedisTemplate(stringRedisTemplate);
    }

    /**
     * 诱导屏工具类初始化
     */
    private void initYdpUtil(){
        YdpInfoUtil.setStringRedisTemplate(stringRedisTemplate);
    }
}
