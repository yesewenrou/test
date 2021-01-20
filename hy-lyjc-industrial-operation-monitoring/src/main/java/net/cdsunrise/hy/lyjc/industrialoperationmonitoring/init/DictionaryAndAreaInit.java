package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.init;

import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.DataDictionaryFeignClient;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.MsmAdministrativeAreaFeignClient;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.helper.DataDictionaryHelper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.helper.MsmAdministrativeAreaHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * DictionaryCodeInit
 *
 * @author LiuYin
 * @date 2020/3/25 15:06
 */
@Component
@Order(1)
public class DictionaryAndAreaInit implements ApplicationRunner {

    @Autowired
    private DataDictionaryFeignClient dataDictionaryFeignClient;
    @Autowired
    private MsmAdministrativeAreaFeignClient msmAdministrativeAreaFeignClient;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        initDictionaryHelper();
        initArea();
    }

    /**
     * 初始化数据字典帮助类
     */
    private void initDictionaryHelper(){
        DataDictionaryHelper.dataDictionaryFeignClient = dataDictionaryFeignClient;
        DataDictionaryHelper.update();
    }

    /**
     * 初始化行政区域省市
     */
    private void initArea(){
        MsmAdministrativeAreaHelper.init(msmAdministrativeAreaFeignClient);
    }
}
