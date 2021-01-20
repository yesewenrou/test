package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.typeenum.DayOfWeek;

import java.util.Date;

/**
 * 指挥调度值班表
 *
 * @author lijiafeng
 * @date 2019/11/25 10:29
 */
@Data
public class DutyRoster {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 值班时间 MONDAY ~ SUNDAY
     */
    private DayOfWeek dutyTime;

    /**
     * 值班人员ID
     */
    private Long memberId;

    /**
     * 值班人员
     */
//    private String dutyPerson;

    /**
     * 值班领导
     */
//    private String dutyLeader;

    /**
     * 联系方式
     */
//    private String contact;

    /**
     * 创建时间
     */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Date gmtCreate;

    /**
     * 更新时间
     */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Date gmtModified;


}
