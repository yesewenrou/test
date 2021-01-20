package net.cdsunrise.hy.lyyt.service.impl;

import lombok.extern.slf4j.Slf4j;

import net.cdsunrise.hy.lyyt.service.InitService;
import net.cdsunrise.hy.lyyt.service.BigDataCenterRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName InitServiceImpl
 * @Description
 * @Author LiuYin
 * @Date 2019/11/6 8:52
 */
@Service
@Slf4j
public class InitServiceImpl implements InitService {

    @Autowired
    private BigDataCenterRedisService bigDataCenterRedisService;

    /**
     * 初始化数据类型
     */
    @Override
    public Long initDataType(){
        bigDataCenterRedisService.initDataType();
        return bigDataCenterRedisService.getDataTypeSize();
    }

    @Override
    public Long initDataSource() {
        bigDataCenterRedisService.initDataSource();
        return bigDataCenterRedisService.getDataSourceSize();
    }

    @Override
    public Long initDataBaseType() {
        bigDataCenterRedisService.initDataBaseType();
        return bigDataCenterRedisService.getDataBaseTypeSize();
    }


}
