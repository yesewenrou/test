package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.cdsunrise.common.utility.AssertUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums.OperationEnum;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.domain.dto.RoleDTO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.domain.vo.resp.RoleVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.enums.BusinessExceptionEnum;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.enums.DeleteEnum;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.enums.StatusEnum;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.exception.BusinessException;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.mapper.RoleMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.service.RoleMenuService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.service.RoleService;
import net.cdsunrise.hy.record.starter.service.RecordService;
import net.cdsunrise.hy.sso.starter.common.CustomContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author LHY
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RoleMenuService roleMenuService;
    @Autowired
    private RecordService recordService;

    private static final int MIN_NAME_LENGTH = 2;
    private static final int MAX_NAME_LENGTH = 100;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(String name, List<Long> menuIdList) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setName(name);
        checkoutParam(roleDTO,menuIdList);
        roleMapper.insert(roleDTO);
        Long roleId = roleDTO.getId();
        roleMenuService.update(roleId,menuIdList);
        // 记录操作日志
        recordService.insert(OperationEnum.ROLE_PERMISSION_CRUD,roleDTO, CustomContext.getTokenInfo().getUser().getId());
        return roleId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, String name, List<Long> menuIdList) {
        AssertUtil.isFalse(name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH || menuIdList.size()==0,
                () -> new BusinessException(BusinessExceptionEnum.PARAM_ERROR));
        RoleDTO roleDTO = roleMapper.selectById(id);
        RoleDTO newRoleDTO = new RoleDTO();
        BeanUtils.copyProperties(roleDTO,newRoleDTO);
        newRoleDTO.setName(name);
        roleMapper.updateById(newRoleDTO);
        roleMenuService.update(id,menuIdList);
        // 记录操作日志
        recordService.update(OperationEnum.ROLE_PERMISSION_CRUD,roleDTO,newRoleDTO,CustomContext.getTokenInfo().getUser().getId());
    }

    @Override
    public void delete(Long id) {
        int row = roleMapper.findByRoleId(id);
        AssertUtil.isFalse(row>0,() -> new BusinessException(BusinessExceptionEnum.ROLE_DELETE_ERROR));
        RoleDTO roleDTO = roleMapper.selectById(id);
        roleDTO.setDeleted(DeleteEnum.DELETE.getCode());
        roleMapper.updateById(roleDTO);
        /**
         * 暂时不对关联表做删除操作，这样当手动恢复角色时，也能恢复菜单关系
         */
        //roleMenuService.delete(id);
        recordService.delete(OperationEnum.ROLE_PERMISSION_CRUD,roleDTO,CustomContext.getTokenInfo().getUser().getId());
    }

    @Override
    public Map<String, Object> findById(Long id) {
        Map<String, Object> resultMap = new HashMap<>();
        RoleDTO roleDTO = roleMapper.selectById(id);
        resultMap.put("roleDTO", roleDTO);
        resultMap.put("menuIdList", roleMenuService.selectByRoleId(id));
        return resultMap;
    }

    @Override
    public void enable(Long id) {
        RoleDTO roleDTO = roleMapper.selectById(id);
        roleDTO.setStatus(StatusEnum.ENABLE.getCode());
        roleMapper.updateById(roleDTO);
        // 不属于“新增，删除，修改，导出”等几项，因此用operation
        recordService.operation(OperationEnum.ROLE_PERMISSION_ENABLE_OR_DISABLE,roleDTO,
                CustomContext.getTokenInfo().getUser().getId(),"启用");
    }

    @Override
    public void disable(Long id) {
        RoleDTO roleDTO = roleMapper.selectById(id);
        roleDTO.setStatus(StatusEnum.DISABLE.getCode());
        roleMapper.updateById(roleDTO);
        recordService.operation(OperationEnum.ROLE_PERMISSION_ENABLE_OR_DISABLE,roleDTO,
                CustomContext.getTokenInfo().getUser().getId(),"禁用");
    }

    @Override
    public List<RoleVO> findByUserId(Long userId) {
        return roleMapper.findByUserId(userId);
    }

    @Override
    public IPage<RoleDTO> list(Integer page, Integer size, String name) {
        Page<RoleDTO> iPage = new Page<>(page, size);
        QueryWrapper<RoleDTO> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .like(StringUtils.isNotEmpty(name), RoleDTO::getName, name)
                .eq(RoleDTO::getDeleted, DeleteEnum.NOT_DELETE.getCode())
                .orderByDesc(RoleDTO::getCreateTime);
        return roleMapper.selectPage(iPage, wrapper);
    }

    @Override
    public List<RoleVO> findByRoleIdList(List<Long> roleIdList) {
        List<RoleVO> resultList = new ArrayList<>();
        if (roleIdList!=null && roleIdList.size()>0){
            QueryWrapper<RoleDTO> wrapper = new QueryWrapper<>();
            wrapper.in("id",roleIdList);
            List<RoleDTO> list = roleMapper.selectList(wrapper);
            list.forEach(roleDTO -> {
                RoleVO roleVO = new RoleVO();
                roleVO.setId(roleDTO.getId());
                roleVO.setName(roleDTO.getName());
                roleVO.setStatus(roleDTO.getStatus());
                resultList.add(roleVO);
            });
        }
        return resultList;
    }

    private void checkoutParam(RoleDTO roleDTO, List<Long> menuIdList) {
        String name = roleDTO.getName();
        if (StringUtils.isNotEmpty(name)) {
            String trimName = name.trim();
            AssertUtil.isFalse(trimName.length() < MIN_NAME_LENGTH || trimName.length() > MAX_NAME_LENGTH || menuIdList.size() == 0,
                    () -> new BusinessException(BusinessExceptionEnum.PARAM_ERROR));
        }
        // 验证角色是否存在（排除更新操作时，不修改角色名情况）
        if (roleDTO.getId() == null || !name.equals(roleMapper.selectById(roleDTO.getId()).getName())) {
            QueryWrapper<RoleDTO> wrapper = new QueryWrapper<>();
            wrapper.lambda()
                    .eq(RoleDTO::getDeleted, DeleteEnum.NOT_DELETE.getCode())
                    .eq(RoleDTO::getName, name);
            RoleDTO role = roleMapper.selectOne(wrapper);
            AssertUtil.isNull(role, () -> new BusinessException(BusinessExceptionEnum.NAME_EXIST));
        }
    }
}
