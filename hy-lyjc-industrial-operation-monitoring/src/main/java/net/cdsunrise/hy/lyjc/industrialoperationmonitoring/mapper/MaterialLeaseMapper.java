package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.MaterialLeaseDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * MaterialTermMapper
 * 物资租赁mapper
 * @author LiuYin
 * @date 2021/1/17 16:13
 */
@Mapper
@Repository
public interface MaterialLeaseMapper extends BaseMapper<MaterialLeaseDO> {
}
