package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.es;

import lombok.Data;

/**
 * @Author: LHY
 * @Date: 2019/9/25 14:26
 * 用于分页搜索条件
 */
@Data
public class PublicSentimentCondition {

    /**标题**/
    private String title;
    /**正面：1，中性：0，负面：-1**/
    private Integer sentiment;

}
