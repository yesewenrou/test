package net.cdsunrise.hy.lyyt.entity.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @ClassName ShareStatisticsVO
 * @Description
 * @Author LiuYin
 * @Date 2019/7/23 10:01
 */
@Data
public class ShareStatisticsVO {

    /** 共享数据总类*/
    private Integer totalType;
    /** 共享数据单位 */
    private Integer totalUnit;
    /** 今日共享条数 */
    private Integer todayShare;
    /** 今日接入条数 */
    private Integer todayConnection;
    /** 正常 */
    private Integer normal;
     /** 异常 */
    private Integer error;


    public static ShareStatisticsVO random(){
        final LocalDateTime now = LocalDateTime.now();
        final int hour = now.getHour();

        final ShareStatisticsVO vo = new ShareStatisticsVO();
        vo.setTotalType(24);
        vo.setTotalUnit(12);
        vo.setTodayShare((int)(System.currentTimeMillis() / 100000000) - 12000);
        vo.setTodayConnection((6));
        vo.setError(0);
        vo.setNormal(vo.getTodayShare() - vo.getError());

        return vo;
    }

    private static Integer r(Integer begin, Integer end){
        return ThreadLocalRandom.current().nextInt(begin, end);
    }


}
