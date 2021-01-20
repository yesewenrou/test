package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author fangyunlong
 * @date 2020/3/7 23:44
 */
@Data
@TableName(value = "hy_auto_warning_scenic")
public class AutoWarningScenic {
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 预警id **/
    private Long warningId;
    /** 游客数 **/
    private Integer peopleNum;
}
