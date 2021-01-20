package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lombok.Data;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.HandleResult;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author LHY
 */
@Repository
public interface HandleResultMapper extends BaseMapper<HandleResult> {

    /**
     * 根据投诉重要性分组统计条数
     *
     * @return 结果
     */
    @Select("SELECT importance, COUNT(*) AS `count` FROM hy_handle_result GROUP BY importance")
    List<ImportanceChartVO> countGroupByImportance();

    @Data
    class ImportanceChartVO {
        private Integer importance;
        private Integer count;
    }
}
