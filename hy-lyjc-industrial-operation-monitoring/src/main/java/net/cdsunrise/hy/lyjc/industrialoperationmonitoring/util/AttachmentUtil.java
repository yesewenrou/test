package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util;

import net.cdsunrise.common.utility.AssertUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.AttachmentDO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums.BusinessTypeEnum;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.exception.ParamErrorException;

/**
 * AttachementUtil
 *
 * @author LiuYin
 * @date 2021/1/17 17:25
 */
public class AttachmentUtil {

    public static AttachmentDO create(String url, Long businessId, BusinessTypeEnum type){
        AssertUtil.notEmpty(url, () -> new ParamErrorException("附件地址为空"));
        AssertUtil.notNull(businessId, () -> new ParamErrorException("附件业务id为空"));
        AssertUtil.notNull(type, () -> new ParamErrorException("附件业务类型为空"));


        final AttachmentDO domain = new AttachmentDO();
        domain.setBusinessId(businessId);
        domain.setBusinessType(type.name().toUpperCase());
        domain.setName("");
        domain.setSuffix("");
        domain.setUrl(url);

        return domain;
    }


}
