package net.cdsunrise.hy.lyyt.entity.resp;

import lombok.Data;
import net.cdsunrise.hy.lyyt.entity.vo.DataGatherStatisticsVO;
import net.cdsunrise.hy.lyyt.entity.vo.DataGatherVO;
import net.cdsunrise.hy.lyyt.enums.DataTypeEnum;

import javax.xml.ws.Holder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @ClassName GatherResponse
 * @Description
 * @Author LiuYin
 * @Date 2019/7/24 16:44
 */
@Data
public class GatherResponse {

    private static final Integer MAX = DataTypeEnum.values().length;


    private List<DataGatherVO> gatherList;

    private List<DataGatherStatisticsVO> gatherStatistics;




    public static GatherResponse random(Integer number, Boolean random){
        if(Objects.isNull(random)){
            random = false;
        }

        if(Objects.isNull(number)){
            number = ThreadLocalRandom.current().nextInt(0, MAX + 1);
            random = false;
        }

        if(number < 0){
            number = 0;
        }

        if(number > MAX){
            number = MAX;
        }
        final GatherResponse response = new GatherResponse();
        response.setGatherList(new ArrayList<>(number));

        List<DataTypeEnum> enums = getDataTypeEnumList(number, random);
        enums.forEach(e -> response.getGatherList().add(DataGatherVO.random(e)));

        final int temp = ThreadLocalRandom.current().nextInt(0, 10);
        final List<DataGatherVO> gatherList = response.getGatherList();
        if(temp < 2 && gatherList.size() > 0){
            gatherList.get(ThreadLocalRandom.current().nextInt(0, gatherList.size() > 5 ? 5 : gatherList.size())).fail();
        }

        int size = gatherList.size();

        response.setGatherStatistics(new ArrayList<>(size));
        gatherList.forEach(g -> {
            response.getGatherStatistics().add(DataGatherStatisticsVO.random(g));
        });
        return response;
    }

    private static List<DataTypeEnum> getDataTypeEnumList(Integer number, Boolean random) {
        if(number == 0){
            return new ArrayList<>(0);
        }
        final int begin = ThreadLocalRandom.current().nextInt(0, MAX);

        int index = random ? ThreadLocalRandom.current().nextInt(0, number + 1) : number;

        List<DataTypeEnum> list = new ArrayList<>(index);
        Holder<Integer> indexHolder = new Holder<>(begin);
        for(int i = 0; i < index; i++){
            list.add(DataTypeEnum.values()[indexHolder.value >= MAX ? indexHolder.value - MAX : indexHolder.value]);
            indexHolder.value += 1;
        }
        return list;
    }
}
