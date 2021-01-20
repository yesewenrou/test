package net.cdsunrise.hy.lyyt.entity.resp;

import lombok.Data;
import net.cdsunrise.common.utility.AssertUtil;
import net.cdsunrise.hy.lyyt.entity.vo.ShareStatisticsVO;
import net.cdsunrise.hy.lyyt.entity.vo.UnitDataVO;
import net.cdsunrise.hy.lyyt.enums.DataDescriptionEnum;
import net.cdsunrise.hy.lyyt.enums.DataSourceUnitEnum;
import net.cdsunrise.hy.lyyt.exception.business.BusinessException;
import net.cdsunrise.hy.lyyt.exception.business.BusinessExceptionEnum;

import java.time.LocalDateTime;

/**
 * @ClassName SourceShareResponse
 * @Description
 * @Author LiuYin
 * @Date 2019/7/23 9:49
 */
@Data
public class SourceShareResponse {

    private Integer index;
    private String name;

    private UnitDataVO dataTotal;
    private UnitDataVO dataCount;
    private UnitDataVO currentDayShareError;
    private UnitDataVO currentDayCallTimes;

    private ShareStatisticsVO shareStatistics;


    public static SourceShareResponse random(Integer index){
        AssertUtil.isTrue(DataSourceUnitEnum.isIndexExist(index), BusinessException.fail(BusinessExceptionEnum.INDEX_ERROR));
        final DataSourceUnitEnum dataSourceUnitEnum = DataSourceUnitEnum.getIndexMap().get(index);
        SourceShareResponse response;
        switch (dataSourceUnitEnum){
            // 如果是网络数据则：
            case WLSJ:
                 response =  createWLSJResponse(dataSourceUnitEnum);
                 break;
            case ZJSB:
                response = createZJSBResponse(dataSourceUnitEnum);
                break;
            default:
                response = createZeroValueResponse(dataSourceUnitEnum);
        }
        response.setShareStatistics(ShareStatisticsVO.random());
        return response;
    }

    /**
     * 创建网络数据的共享信息
     * @return
     */
    private static SourceShareResponse createWLSJResponse(DataSourceUnitEnum dataSourceUnitEnum) {
        LocalDateTime now  = LocalDateTime.now();
        final SourceShareResponse response = new SourceShareResponse();

        response.setIndex(dataSourceUnitEnum.getIndex());
        response.setName(dataSourceUnitEnum.getName());

        response.setDataTotal(UnitDataVO.create(DataDescriptionEnum.DATA_TOTAL, 142*1024*1024L + now.getHour()*60* 10));
        response.setCurrentDayCallTimes((UnitDataVO.create(DataDescriptionEnum.TODAY_CALL_TIMES, (long)(now.getHour()*60*1.75))));
        response.setCurrentDayShareError(UnitDataVO.create(DataDescriptionEnum.CURRENT_DAY_SHARE_ERROR, 0L));
        response.setDataCount(UnitDataVO.create(DataDescriptionEnum.DATA_COUNT, (long)(216484 + now.getHour()*60 * 9.7)));
        return response;
    }

    /**
     * 创建自建设备的共享信息
     * @param dataSourceUnitEnum
     * @return
     */
    private static SourceShareResponse createZJSBResponse(DataSourceUnitEnum dataSourceUnitEnum){

        final SourceShareResponse response = new SourceShareResponse();

        response.setIndex(dataSourceUnitEnum.getIndex());
        response.setName(dataSourceUnitEnum.getName());
        LocalDateTime now  = LocalDateTime.now();

        response.setDataTotal( UnitDataVO.create(DataDescriptionEnum.DATA_TOTAL, 432*1024*1024L + now.getHour() * 60 * 30));
        response.setCurrentDayCallTimes(UnitDataVO.create(DataDescriptionEnum.TODAY_CALL_TIMES, (long)(now.getHour()*60*3.75)));
        response.setCurrentDayShareError(UnitDataVO.create(DataDescriptionEnum.CURRENT_DAY_SHARE_ERROR, 0L));
        response.setDataCount(UnitDataVO.create(DataDescriptionEnum.DATA_COUNT, (long)(146484 + now.getHour()*60 * 5.7 )));

        return response;
    }

    /**
     * 创建零值响应
     * @param dataSourceUnitEnum
     * @return
     */
    private static SourceShareResponse createZeroValueResponse(DataSourceUnitEnum dataSourceUnitEnum){

        final SourceShareResponse response = new SourceShareResponse();

        response.setIndex(dataSourceUnitEnum.getIndex());
        response.setName(dataSourceUnitEnum.getName());

        response.setDataTotal( UnitDataVO.create(DataDescriptionEnum.DATA_TOTAL, 0L));
        response.setCurrentDayCallTimes(UnitDataVO.create(DataDescriptionEnum.TODAY_CALL_TIMES, 0L));
        response.setCurrentDayShareError(UnitDataVO.create(DataDescriptionEnum.CURRENT_DAY_SHARE_ERROR, 0L));
        response.setDataCount(UnitDataVO.create(DataDescriptionEnum.DATA_COUNT, 0L));
        return response;
    }

}
