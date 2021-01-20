package net.cdsunrise.hy.lyyt.entity.vo;

import lombok.Data;
import net.cdsunrise.hy.lyyt.enums.DataDescriptionEnum;
import org.springframework.util.unit.DataUnit;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @ClassName TotalStatisticsVO
 * @Description
 * @Author LiuYin
 * @Date 2019/7/22 18:23
 */
@Data
public class TotalStatisticsVO {

    /** 接入服务总数*/
    private Long totalConnect;
    private String totalConnectUnit = "个";
    /** 总用户数*/
    private Long totalUser;
    private String  totalUserUnit="个";
    /** 服务总量访问*/
    private Long totalVisit;
    private String totalVisitUnit= "次";
    /** 数据总量*/
    private Long totalData;
    private String totalDataUnit;
    /** 数据数量*/
    private Long totalDataCount;
    private String totalDataCountUnit;
    /** 异常调用数*/
    private Long totalExceptionCount;
    private String totalExceptionCountUnit = "条";

    public static TotalStatisticsVO random(){
        final TotalStatisticsVO vo = new TotalStatisticsVO();
        vo.setTotalConnect(r(100000L,10000000L));
        vo.setTotalUser(r(10000,100000));
        vo.setTotalVisit(r(100000,10000000));
        vo.setTotalData(r(100000,100000000));
        vo.setTotalDataCount(r(100000,10000000));
        vo.setTotalExceptionCount(r(100,1000));
        return vo;
    }


    public TotalStatisticsVO complete(){
        final UnitDataVO totalDataUnitDataVO = UnitDataVO.create(DataDescriptionEnum.DATA_TOTAL, getTotalData());
        setTotalData(totalDataUnitDataVO.getNumber().longValue());
        setTotalDataUnit(totalDataUnitDataVO.getUnit());

        final UnitDataVO totalDataCountUnitDataVO = UnitDataVO.create(DataDescriptionEnum.DATA_COUNT, getTotalDataCount());
        setTotalDataCount(totalDataCountUnitDataVO.getNumber().longValue());
        setTotalDataCountUnit(totalDataCountUnitDataVO.getUnit());

        return this;

    }




    private static Long r(long begin, long end){
        return ThreadLocalRandom.current().nextLong(begin, end);
    }

}
