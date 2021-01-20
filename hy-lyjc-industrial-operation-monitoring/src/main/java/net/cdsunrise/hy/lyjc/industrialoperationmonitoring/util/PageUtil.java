package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.cdsunrise.common.utility.vo.PageResult;
import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.exception.OrderByColumnNotSupportException;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分页相关工具
 *
 * @author lijiafeng
 * @date 2019/7/2 13:23
 */
public class PageUtil {

    /**
     * 使用QueryWrapper进行排序
     *
     * @param queryWrapper  查询wrapper
     * @param orderColumns  可排序字段列表
     * @param orderItemList 排序字段列表
     */
    public static void setOrders(QueryWrapper<?> queryWrapper, List<String> orderColumns, List<PageRequest.OrderItem> orderItemList) {
        if (!CollectionUtils.isEmpty(orderItemList)) {
            for (PageRequest.OrderItem orderItem : orderItemList) {
                String column = orderItem.getColumn();
                if (!orderColumns.contains(column)) {
                    throw new OrderByColumnNotSupportException("不支持的排序字段：" + column);
                }
                queryWrapper.orderBy(true, orderItem.getAsc(), column);
            }
        }
    }

    public static <T, R> Result<PageResult<R>> convertResult(Result<PageResult<T>> origin, Function<T, R> mapper) {
        Result<PageResult<R>> result = new Result<>();
        result.setCode(origin.getCode());
        result.setMessage(origin.getMessage());
        result.setSuccess(origin.getSuccess());
        if (origin.getSuccess()) {
            PageResult<R> pageResult = new PageResult<>();
            PageResult<T> originData = origin.getData();
            pageResult.setCurrentSize(originData.getCurrentSize());
            pageResult.setPageNum(originData.getPageNum());
            pageResult.setTotalCount(originData.getTotalCount());
            pageResult.setTotalPages(originData.getTotalPages());
            pageResult.setList(originData.getList().stream().map(mapper).collect(Collectors.toList()));
            result.setData(pageResult);
        }
        return result;
    }

    public static <T, R> PageResult<R> convertPage(Page<T> origin, Function<T, R> mapper) {
        PageResult<R> pageResult = new PageResult<>();
        pageResult.setPageNum((long) origin.getPageable().getPageNumber() + 1);
        pageResult.setCurrentSize((long) origin.getSize());
        pageResult.setTotalPages((long) origin.getTotalPages());
        pageResult.setTotalCount(origin.getTotalElements());
        pageResult.setList(origin.stream().map(mapper).collect(Collectors.toList()));

        return pageResult;
    }

    /**
     * page工具类使用
     *
     * @param page   mybatis plus返回的page数据
     * @param mapper 转换方法
     * @param <T>    入参泛型
     * @param <R>    返回泛型
     * @return pageResult
     */
    public static <T, R> PageResult<R> page(IPage<T> page, Function<T, R> mapper) {
        PageResult<R> pageResult = new PageResult<>();
        pageResult.setCurrentSize((long) page.getRecords().size());
        pageResult.setList(page.getRecords().parallelStream().map(mapper).collect(Collectors.toList()));
        pageResult.setPageNum(page.getCurrent());
        pageResult.setTotalCount(page.getTotal());
        pageResult.setTotalPages(page.getPages());
        return pageResult;
    }

    public static <T, R> PageResult<R> emptyPage(IPage<T> page){
        PageResult<R> pageResult = new PageResult<>();
        pageResult.setCurrentSize((long) page.getRecords().size());
        pageResult.setList(new ArrayList<>());
        pageResult.setPageNum(page.getCurrent());
        pageResult.setTotalCount(page.getTotal());
        pageResult.setTotalPages(page.getPages());
        return pageResult;
    }
}
