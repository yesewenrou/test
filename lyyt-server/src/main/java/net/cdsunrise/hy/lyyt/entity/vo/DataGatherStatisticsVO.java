package net.cdsunrise.hy.lyyt.entity.vo;

import lombok.Data;
import net.cdsunrise.hy.lyyt.enums.DataDescriptionEnum;
import net.cdsunrise.hy.lyyt.enums.DataTypeEnum;
import net.cdsunrise.hy.lyyt.enums.GatherStatusEnum;


/**
 * @ClassName DataGatherStatisticsVO
 * @Description 数据采集统计视图对象
 * @Author LiuYin
 * @Date 2019/7/24 17:22
 */
@Data
public class DataGatherStatisticsVO {

    /** 数据类型*/
    private DataTypeEnum type;
    /** 数据名称*/
    private String name;
    /** 数据状态*/
    private Integer status;
    /** 单位数据对象*/
    private UnitDataVO gatherTotal;



    public static DataGatherStatisticsVO create(DataTypeEnum typeEnum){
        final DataGatherStatisticsVO vo = new DataGatherStatisticsVO();
        vo.setType(typeEnum);
        vo.setName(typeEnum.getTitle());
        vo.setStatus(GatherStatusEnum.SUCCESS.getStatus());

        return vo;
    }


    public static DataGatherStatisticsVO random(DataGatherVO gatherVO){
        final DataGatherStatisticsVO dataGatherStatisticsVO = new DataGatherStatisticsVO();
        dataGatherStatisticsVO.setType(gatherVO.getType());
        dataGatherStatisticsVO.setName(gatherVO.getName());
        dataGatherStatisticsVO.setStatus(gatherVO.getStatus());
        dataGatherStatisticsVO.setGatherTotal(UnitDataVO.random(DataDescriptionEnum.GATHER_TOTAL));
        if(GatherStatusEnum.FAIL.getStatus().equals(dataGatherStatisticsVO.getStatus())){
            dataGatherStatisticsVO.getGatherTotal().setValue("");
            dataGatherStatisticsVO.getGatherTotal().setNumber(0);
        }
        return dataGatherStatisticsVO;

    }

}
