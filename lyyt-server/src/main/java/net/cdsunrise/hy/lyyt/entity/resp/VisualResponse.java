package net.cdsunrise.hy.lyyt.entity.resp;

import lombok.Data;
import net.cdsunrise.common.utility.AssertUtil;
import net.cdsunrise.hy.lyyt.entity.vo.TotalStatisticsVO;
import net.cdsunrise.hy.lyyt.entity.vo.UnitDataVO;
import net.cdsunrise.hy.lyyt.enums.DataDescriptionEnum;
import net.cdsunrise.hy.lyyt.enums.DataTypeEnum;
import net.cdsunrise.hy.lyyt.enums.UnitEnum;
import net.cdsunrise.hy.lyyt.exception.business.BusinessException;
import net.cdsunrise.hy.lyyt.exception.business.BusinessExceptionEnum;


/**
 * @ClassName VisualResponse
 * @Description
 * @Author LiuYin
 * @Date 2019/7/22 16:31
 */
@Data
public class VisualResponse {


    private Integer index;
    private String title;

    private UnitDataVO dataTotal;
    private UnitDataVO dataCount;
    private UnitDataVO totalCallTimes;
    private UnitDataVO todayCallTimes;

    private TotalStatisticsVO totalStatistics;



    public static VisualResponse random(Integer index){
        AssertUtil.isTrue(DataTypeEnum.isIndexExists(index), BusinessException.fail(BusinessExceptionEnum.INDEX_ERROR));
        final VisualResponse visualResponse = new VisualResponse();

        final DataTypeEnum dataTypeEnum = DataTypeEnum.getIndexMap().get(index);
        visualResponse.setTitle(dataTypeEnum.getTitle());
        visualResponse.setIndex(dataTypeEnum.getIndex());

        visualResponse.setDataTotal(UnitDataVO.random(DataDescriptionEnum.DATA_TOTAL));
        visualResponse.setDataCount(UnitDataVO.random(DataDescriptionEnum.DATA_COUNT));
        visualResponse.setTotalCallTimes(UnitDataVO.random(DataDescriptionEnum.TOTAL_CALL_TIMES));
        visualResponse.setTodayCallTimes(UnitDataVO.random(DataDescriptionEnum.TODAY_CALL_TIMES));

        // 如果是旅游停车基础数据，那么
        if(DataTypeEnum.LYTCJCSJ.getIndex().equals(index)){
            visualResponse.getDataTotal().setNumber(19);
            visualResponse.getDataTotal().setUnit(UnitEnum.KB.getName());

            visualResponse.getDataCount().setNumber(19);
            visualResponse.getDataCount().setUnit(UnitEnum.TIAO.getName());
        }

        // 如果是旅游商户资源基础数据，那么
        if(DataTypeEnum.LYSHZYJCSJ.getIndex().equals(index)){
            visualResponse.getDataTotal().setNumber(3);
            visualResponse.getDataTotal().setUnit(UnitEnum.MB.getName());

            visualResponse.getDataCount().setNumber(2951);
            visualResponse.getDataCount().setUnit(UnitEnum.TIAO.getName());
        }

        visualResponse.setTotalStatistics(TotalStatisticsVO.random());
        return visualResponse;
    }

}
