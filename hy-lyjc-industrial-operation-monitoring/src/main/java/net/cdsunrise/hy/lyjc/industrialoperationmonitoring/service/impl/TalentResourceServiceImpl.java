package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import net.cdsunrise.common.utility.vo.PageResult;
import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lydsjdatacenter.starter.feign.EmployedPersonClient;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums.TalentResourceTypeEnum;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.exception.ParamErrorException;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.ITalentResourceService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.PageUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.TalentResourceVO;
import org.springframework.stereotype.Service;

/**
 * @author lijiafeng
 * @date 2020/1/17 11:19
 */
@Service
public class TalentResourceServiceImpl implements ITalentResourceService {

    private EmployedPersonClient employedPersonClient;

    public TalentResourceServiceImpl(EmployedPersonClient employedPersonClient) {
        this.employedPersonClient = employedPersonClient;
    }

    @Override
    public Result<PageResult<TalentResourceVO>> listTalentResources(int page, int size, String type, String name, String unitAndPosition) {
        TalentResourceTypeEnum typeEnum = checkResourceType(type);
        switch (typeEnum) {
            // 中英文讲解员
            case NARRATOR:
                return PageUtil.convertResult(
                        employedPersonClient.queryChineseEnglishGuide(page, size, name, null, unitAndPosition),
                        chineseEnglishGuideVO -> {
                            TalentResourceVO talentResourceVO = new TalentResourceVO();
                            talentResourceVO.setId(chineseEnglishGuideVO.getId());
                            talentResourceVO.setName(chineseEnglishGuideVO.getUserName());
                            talentResourceVO.setType(typeEnum.getName());
                            talentResourceVO.setSex(chineseEnglishGuideVO.getGender());
                            talentResourceVO.setEducation(chineseEnglishGuideVO.getEducation());
                            talentResourceVO.setPhoneNumber(chineseEnglishGuideVO.getContactPhone());
                            talentResourceVO.setUnitAndPosition(chineseEnglishGuideVO.getUnitAndPosition());

                            // 信用等级
                            talentResourceVO.setIntegrityLevel(chineseEnglishGuideVO.getIntegrityLevel());
                            talentResourceVO.setIntegrityLevelDesc(chineseEnglishGuideVO.getIntegrityLevelDesc());
                            return talentResourceVO;
                        }
                );
            case WORKER:
                return PageUtil.convertResult(
                        employedPersonClient.queryTourismProfessional(page, size, name, null, unitAndPosition),
                        tourismProfessionalsVO -> {
                            TalentResourceVO talentResourceVO = new TalentResourceVO();
                            talentResourceVO.setId(tourismProfessionalsVO.getId());
                            talentResourceVO.setName(tourismProfessionalsVO.getUserName());
                            talentResourceVO.setType(typeEnum.getName());
                            talentResourceVO.setSex(tourismProfessionalsVO.getGender());
                            talentResourceVO.setEducation(tourismProfessionalsVO.getEducation());
                            talentResourceVO.setPhoneNumber(tourismProfessionalsVO.getContactPhone());
                            talentResourceVO.setUnitAndPosition(tourismProfessionalsVO.getUnitAndPosition());

                            // 信用等级
                            talentResourceVO.setIntegrityLevel(tourismProfessionalsVO.getIntegrityLevel());
                            talentResourceVO.setIntegrityLevelDesc(tourismProfessionalsVO.getIntegrityLevelDesc());
                            return talentResourceVO;
                        }
                );
            default:
                throw new ParamErrorException("资源类型不正确");
        }
    }

    private TalentResourceTypeEnum checkResourceType(String type) {
        TalentResourceTypeEnum typeEnum = TalentResourceTypeEnum.getByCode(type);
        if (typeEnum == null) {
            throw new ParamErrorException("资源类型不正确");
        }
        return typeEnum;
    }
}
