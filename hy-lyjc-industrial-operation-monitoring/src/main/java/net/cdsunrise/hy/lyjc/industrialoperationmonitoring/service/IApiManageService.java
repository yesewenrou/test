package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.ApiManage;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.ApiManagePageRequest;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.ApiManageVO;

/**
 * @author lijiafeng
 * @date 2020/05/14 14:48
 */
public interface IApiManageService extends IService<ApiManage> {

    /**
     * 分页查询
     *
     * @param req 请求
     * @return 结果
     */
    IPage<ApiManageVO> listApiManage(ApiManagePageRequest req);
}
