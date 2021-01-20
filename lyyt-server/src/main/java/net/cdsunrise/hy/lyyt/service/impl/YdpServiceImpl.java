package net.cdsunrise.hy.lyyt.service.impl;

import net.cdsunrise.hy.lyyt.entity.vo.YdpInfoVO;
import net.cdsunrise.hy.lyyt.service.YdpService;
import net.cdsunrise.hy.lyyt.utils.YdpInfoUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName YdpServiceImpl
 * @Description 诱导屏服务实现
 * @Author LiuYin
 * @Date 2019/12/21 10:27
 */
@Service
public class YdpServiceImpl implements YdpService {


    @Override
    public List<YdpInfoVO> getAll() {
        // 先获取诱导屏名称
        final Map<String, String> allNameMap = YdpInfoUtil.getAllName();
        if(CollectionUtils.isEmpty(allNameMap)){
            return new ArrayList<>(0);
        }
        final List<YdpInfoVO> list = allNameMap.entrySet().stream().map(YdpServiceImpl::entryToVO).collect(Collectors.toList());

        // 设置诱导屏状态
        final Map<String, String> allStatusMap = YdpInfoUtil.getAllStatus();
        list.forEach(vo -> addStatus(allStatusMap, vo));
        // 排序后返回
        return list.stream().sorted(Comparator.comparingLong(YdpInfoVO::getId)).collect(Collectors.toList());
    }

    /**
     * 将entry中的键值转换成诱导屏对象
     * @param entry &lt;string,string&gt;
     * @return 诱导屏视图对象
     */
    private static YdpInfoVO entryToVO(Map.Entry<String,String> entry){
        return YdpInfoVO.create(Long.parseLong(entry.getKey()),entry.getValue());
    }

    /**
     * 给诱导屏添加状态
     * @param statusMap 状态map
     * @param vo 诱导屏视图对象
     */
    private static void addStatus(Map<String,String> statusMap, YdpInfoVO vo){
        if(Objects.nonNull(statusMap)){
            final String status = statusMap.get(String.valueOf(vo.getId()));
            vo.setStatus(Objects.nonNull(status) ? status : YdpInfoUtil.getDefaultStatus());
        }else{
            vo.setStatus(YdpInfoUtil.getDefaultStatus());
        }
    }
}
