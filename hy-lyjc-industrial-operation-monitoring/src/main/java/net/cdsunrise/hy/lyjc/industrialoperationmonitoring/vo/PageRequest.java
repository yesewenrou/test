package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;
import net.cdsunrise.common.utility.vo.group.ConditionNotNullGroup;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 分页请求类
 *
 * @author lijiafeng
 * @date 2019/8/6 10:20
 */
@Data
public class PageRequest<T> {

    /**
     * 页码
     */
    @NotNull(message = "页码不能为空")
    private Long current;

    /**
     * 每页条数 设置为-1时，则不分页
     */
    @NotNull(message = "每页条数不能为空")
    private Long size;

    /**
     * 排序规则列表
     */
    @Valid
    private List<OrderItem> orderItemList;

    /**
     * 分页筛选条件
     */
    @Valid
    @NotNull(message = "查询条件不能为空", groups = {ConditionNotNullGroup.class})
    private T condition;

    @Data
    public static class OrderItem {

        /**
         * 排序列名
         */
        @NotBlank(message = "排序列名不能为空")
        private String column;

        /**
         * 是否正序排序
         */
        @NotNull(message = "是否正序排序不能为空")
        private Boolean asc;
    }
}
