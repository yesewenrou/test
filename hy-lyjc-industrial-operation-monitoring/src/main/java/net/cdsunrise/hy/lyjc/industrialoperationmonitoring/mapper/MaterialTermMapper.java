package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.MaterialTermDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * MaterialTermMapper
 *
 * @author LiuYin
 * @date 2021/1/17 16:13
 */
@Mapper
@Repository
public interface MaterialTermMapper extends BaseMapper<MaterialTermDO> {
}
