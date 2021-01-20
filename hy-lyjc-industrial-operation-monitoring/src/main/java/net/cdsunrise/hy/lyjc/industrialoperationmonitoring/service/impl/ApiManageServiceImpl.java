package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.ApiManage;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.mapper.ApiManageMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IApiManageService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.ApiManagePageRequest;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.ApiManageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author lijiafeng
 * @date 2020/05/14 14:53
 */
@Service
public class ApiManageServiceImpl extends ServiceImpl<ApiManageMapper, ApiManage> implements IApiManageService {

    @Override
    public IPage<ApiManageVO> listApiManage(ApiManagePageRequest req) {
        // 分页
        Page<ApiManage> page = new Page<>(req.getPage(), req.getSize());
        QueryWrapper<ApiManage> queryWrapper = Wrappers.query();
        //筛选
        queryWrapper.lambda()
                .eq(StringUtils.hasText(req.getDataSource()), ApiManage::getDataSource, req.getDataSource())
                .orderByDesc(ApiManage::getGmtModified);
        super.page(page, queryWrapper);
        // 转换结果
        return page.convert(apiManage -> {
            ApiManageVO apiManageVO = new ApiManageVO();
            BeanUtils.copyProperties(apiManage, apiManageVO);
            return apiManageVO;
        });
    }
}
