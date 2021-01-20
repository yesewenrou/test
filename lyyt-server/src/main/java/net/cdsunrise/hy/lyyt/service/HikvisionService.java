package net.cdsunrise.hy.lyyt.service;

import com.alibaba.fastjson.JSONObject;

/**
 * @Author: suzhouhe @Date: 2020/1/18 17:50 @Description:
 */
public interface HikvisionService {

    /**
     * 获取摄像头数据
     * @param url
     * @param jsonObject
     * @return
     */
    String getCameras(String url, JSONObject jsonObject);
}
