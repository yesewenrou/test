package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.vo;

import lombok.Data;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums.RegionTypeEnum;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * TouristRegionStatisticsVO
 *
 * @author LiuYin
 * @date 2020/3/25 14:42
 */
@Data
public class TouristRegionStatisticsVO{

    /** 区域名称，可能是国家，也可能是省市地区*/
    private String regionName;

    /**
     * 全名称
     */
    private String fullName;

    /** 人数*/
    private Long peopleNum;

    /**
     * 区域类型，{@link net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums.RegionTypeEnum}
     */
    private Integer regionType;

    /**
     * 子区域列表
     */
    private List<TouristRegionStatisticsVO> children;


    public void countFromChildren(){
        if(Objects.nonNull(children) && !children.isEmpty()){
            peopleNum += children.stream().filter(Objects::nonNull).filter(v -> Objects.nonNull(v.getPeopleNum())).mapToLong(TouristRegionStatisticsVO::getPeopleNum).sum();
        }
    }


    public static TouristRegionStatisticsVO createDefault(RegionTypeEnum regionType){
        final TouristRegionStatisticsVO vo = new TouristRegionStatisticsVO();
        vo.setRegionType(regionType.getType());
        vo.setPeopleNum(0L);
        vo.setChildren(new ArrayList<>());
        return vo;
    }

    public static TouristRegionStatisticsVO createChina() {
        final TouristRegionStatisticsVO chinaVO = createDefault(RegionTypeEnum.CHINA);
        chinaVO.setRegionName("中国");
        return chinaVO;
    }

    public void sortChildrenByDesc(){
        if(Objects.isNull(children) || children.isEmpty()){
            return;
        }
        children.sort((o1, o2) -> o2.getPeopleNum().compareTo(o1.getPeopleNum()));
        children.forEach(TouristRegionStatisticsVO::sortChildrenByDesc);
    }


    public void addFullName(){
        addFullName(null);
    }

    public void addFullName(String parentFullName){
        if(Objects.isNull(parentFullName) || parentFullName.isEmpty()){
            fullName = regionName;
        }else{
            fullName = parentFullName + "_" + regionName;
        }
        if(hasChildren()){
            children.forEach(child -> child.addFullName(fullName));
        }
    }

    private boolean hasChildren(){
        return Objects.nonNull(children) && !children.isEmpty();
    }

}
