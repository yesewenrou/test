package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums;

import net.cdsunrise.common.utility.enums.OperationTypeEnum;
import net.cdsunrise.common.utility.inter.ProjectModel;
import net.cdsunrise.common.utility.obj.ModelInfo;
import net.cdsunrise.common.utility.obj.RecordQueryWrapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.ComplaintManage;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.FeatureLabel;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.FeatureTourismResource;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.domain.dto.PlatformUserLgDTO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.domain.dto.RoleDTO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.enums.BusinessExceptionEnum;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.exception.BusinessException;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.IndustryManageVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.LogHandleResultVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.ResourceVO;

/**
 * @Author: LHY
 * @Date: 2019/9/20 17:07
 */
public enum OperationEnum implements ProjectModel{

    /**
     * 行业管理
     */
    INDUSTRY_MANAGE(ModelEnum.INDUSTRY_MANAGE){
        @Override
        public String getSummary(OperationTypeEnum operationTypeEnum){
            switch (operationTypeEnum) {
                case DELETE:
                case UPDATE:
                case INSERT:
                    return operationTypeEnum.getName();
                default:
                    throw new BusinessException(BusinessExceptionEnum.USER_OPERATION_ERROR);
            }
        }
        @Override
        public ModelInfo getModelInfo() {
            return this.getModel();
        }
        @Override
        public RecordQueryWrapper<IndustryManageVO> getWrapper(OperationTypeEnum type) {
            switch (type){
                case INSERT:
                case UPDATE:
                    return new RecordQueryWrapper<IndustryManageVO>().select(IndustryManageVO::getName,IndustryManageVO::getArea,
                            IndustryManageVO::getType,IndustryManageVO::getLocation,IndustryManageVO::getServicePhone,
                            IndustryManageVO::getOperatingSubject,IndustryManageVO::getLongitude,IndustryManageVO::getLatitude);
                case DELETE:
                    return new RecordQueryWrapper<IndustryManageVO>().select(IndustryManageVO::getName);
                default:
                    throw new BusinessException(BusinessExceptionEnum.USER_OPERATION_ERROR);
            }
        }
    },

    /**
     * 旅游管理
     */
    TOURISM_MANAGE(ModelEnum.TOURISM_MANAGE){
        @Override
        public String getSummary(OperationTypeEnum operationTypeEnum){
            switch (operationTypeEnum) {
                case DELETE:
                case UPDATE:
                case INSERT:
                    return operationTypeEnum.getName();
                default:
                    throw new BusinessException(BusinessExceptionEnum.USER_OPERATION_ERROR);
            }
        }
        @Override
        public ModelInfo getModelInfo() {
            return this.getModel();
        }
        @Override
        public RecordQueryWrapper<ResourceVO> getWrapper(OperationTypeEnum type) {
            switch (type){
                case INSERT:
                case UPDATE:
                    return new RecordQueryWrapper<ResourceVO>().select(ResourceVO::getName,ResourceVO::getArea,
                            ResourceVO::getType,ResourceVO::getLocation,ResourceVO::getServicePhone,
                            ResourceVO::getLongitude,ResourceVO::getLatitude);
                case DELETE:
                    return new RecordQueryWrapper<ResourceVO>().select(ResourceVO::getName);
                default:
                    throw new BusinessException(BusinessExceptionEnum.USER_OPERATION_ERROR);
            }
        }
    },

    /**
     * 公共管理
     */
    PUBLIC_MANAGE(ModelEnum.PUBLIC_MANAGE){
        @Override
        public String getSummary(OperationTypeEnum operationTypeEnum){
            switch (operationTypeEnum) {
                case DELETE:
                case UPDATE:
                case INSERT:
                    return operationTypeEnum.getName();
                default:
                    throw new BusinessException(BusinessExceptionEnum.USER_OPERATION_ERROR);
            }
        }
        @Override
        public ModelInfo getModelInfo() {
            return this.getModel();
        }
        @Override
        public RecordQueryWrapper<ResourceVO> getWrapper(OperationTypeEnum type) {
            switch (type){
                case INSERT:
                case UPDATE:
                    return new RecordQueryWrapper<ResourceVO>().select(ResourceVO::getName,ResourceVO::getArea,
                            ResourceVO::getType,ResourceVO::getLocation,ResourceVO::getServicePhone,
                            ResourceVO::getLongitude,ResourceVO::getLatitude);
                case DELETE:
                    return new RecordQueryWrapper<ResourceVO>().select(ResourceVO::getName);
                default:
                    throw new BusinessException(BusinessExceptionEnum.USER_OPERATION_ERROR);
            }
        }
    },

    /**
     * 投诉管理
     */
    COMPLAINT_MANAGE(ModelEnum.COMPLAINT_MANAGE){
        @Override
        public String getSummary(OperationTypeEnum operationTypeEnum){
            if (OperationTypeEnum.OPERATION == operationTypeEnum){
                return operationTypeEnum.getName();
            }
            throw new BusinessException(BusinessExceptionEnum.USER_OPERATION_ERROR);
        }
        @Override
        public ModelInfo getModelInfo() {
            return this.getModel();
        }
        @Override
        public RecordQueryWrapper<ComplaintManage> getWrapper(OperationTypeEnum type) {
            if (OperationTypeEnum.OPERATION == type) {
                return new RecordQueryWrapper<ComplaintManage>().select(ComplaintManage::getComplaintNumber);
            }
            throw new BusinessException(BusinessExceptionEnum.USER_OPERATION_ERROR);
        }
    },

    /**
     * 处理结果
     */
    HANDLE_RESULT(ModelEnum.HANDLE_RESULT){
        @Override
        public String getSummary(OperationTypeEnum operationTypeEnum){
            if (OperationTypeEnum.OPERATION == operationTypeEnum){
                return operationTypeEnum.getName();
            }
            throw new BusinessException(BusinessExceptionEnum.USER_OPERATION_ERROR);
        }
        @Override
        public ModelInfo getModelInfo() {
            return this.getModel();
        }
        @Override
        public RecordQueryWrapper<LogHandleResultVO> getWrapper(OperationTypeEnum type) {
            if (OperationTypeEnum.OPERATION == type) {
                return new RecordQueryWrapper<LogHandleResultVO>().all();
            }
            throw new BusinessException(BusinessExceptionEnum.USER_OPERATION_ERROR);
        }
    },

    /**
     * 平台用户-角色配置
     */
    PLATFORM_USER(ModelEnum.PLATFORM_USER) {
        @Override
        public String getSummary(OperationTypeEnum operationTypeEnum) {
            if (OperationTypeEnum.OPERATION == operationTypeEnum) {
                return operationTypeEnum.getName();
            }
            throw new BusinessException(BusinessExceptionEnum.USER_OPERATION_ERROR);
        }

        @Override
        public ModelInfo getModelInfo() {
            return this.getModel();
        }

        @Override
        public RecordQueryWrapper<PlatformUserLgDTO> getWrapper(OperationTypeEnum type) {
            if (OperationTypeEnum.OPERATION == type) {
                return new RecordQueryWrapper<PlatformUserLgDTO>().all();
            }
            throw new BusinessException(BusinessExceptionEnum.USER_OPERATION_ERROR);
        }
    },

    /**
     * 角色权限-crud
     */
    ROLE_PERMISSION_CRUD(ModelEnum.ROLE_PERMISSION) {
        @Override
        public String getSummary(OperationTypeEnum operationTypeEnum) {
            switch (operationTypeEnum) {
                case DELETE:
                case UPDATE:
                case INSERT:
                    return operationTypeEnum.getName();
                default:
                    throw new BusinessException(BusinessExceptionEnum.USER_OPERATION_ERROR);
            }
        }

        @Override
        public ModelInfo getModelInfo() {
            return this.getModel();
        }

        @Override
        public RecordQueryWrapper<RoleDTO> getWrapper(OperationTypeEnum type) {
            switch (type) {
                case DELETE:
                case UPDATE:
                case INSERT:
                    return new RecordQueryWrapper<RoleDTO>().select(RoleDTO::getName);
                default:
                    throw new BusinessException(BusinessExceptionEnum.USER_OPERATION_ERROR);
            }
        }
    },

    /**
     * 角色权限-启用/禁用
     */
    ROLE_PERMISSION_ENABLE_OR_DISABLE(ModelEnum.ROLE_PERMISSION) {
        @Override
        public String getSummary(OperationTypeEnum operationTypeEnum) {
            if (operationTypeEnum == OperationTypeEnum.OPERATION) {
                return operationTypeEnum.getName();
            }
            throw new BusinessException(BusinessExceptionEnum.USER_OPERATION_ERROR);
        }

        @Override
        public ModelInfo getModelInfo() {
            return this.getModel();
        }

        @Override
        public RecordQueryWrapper<RoleDTO> getWrapper(OperationTypeEnum type) {
            if (type == OperationTypeEnum.OPERATION) {
                return new RecordQueryWrapper<RoleDTO>().select(RoleDTO::getName);
            }
            throw new BusinessException(BusinessExceptionEnum.USER_OPERATION_ERROR);
        }
    },
    /**
     * 特色标签
     */
    FEATURE_LABEL(ModelEnum.FEATURE_LABEL){
        @Override
        @SuppressWarnings("Duplicates")
        public String getSummary(OperationTypeEnum operationTypeEnum){
            switch (operationTypeEnum) {
                case DELETE:
                case UPDATE:
                case INSERT:
                    return operationTypeEnum.getName();
                default:
                    throw new BusinessException(BusinessExceptionEnum.USER_OPERATION_ERROR);
            }
        }
        @Override
        public ModelInfo getModelInfo() {
            return this.getModel();
        }
        @Override
        public RecordQueryWrapper<FeatureLabel> getWrapper(OperationTypeEnum type) {
            switch (type){
                case INSERT:
                case UPDATE:
                    return new RecordQueryWrapper<FeatureLabel>().select(FeatureLabel::getLabelName,FeatureLabel::getUpdateBy,
                            FeatureLabel::getUpdateName,FeatureLabel::getUpdateTime);
                case DELETE:
                    return new RecordQueryWrapper<FeatureLabel>().select(FeatureLabel::getLabelName);
                default:
                    throw new BusinessException(BusinessExceptionEnum.USER_OPERATION_ERROR);
            }
        }
    },
    /**
     * 特色旅游资源
     */
    FEATURE_TOURISM_RESOURCE(ModelEnum.FEATURE_TOURISM_RESOURCE){
        @Override
        @SuppressWarnings("Duplicates")
        public String getSummary(OperationTypeEnum operationTypeEnum){
            switch (operationTypeEnum) {
                case DELETE:
                case UPDATE:
                case INSERT:
                    return operationTypeEnum.getName();
                default:
                    throw new BusinessException(BusinessExceptionEnum.USER_OPERATION_ERROR);
            }
        }
        @Override
        public ModelInfo getModelInfo() {
            return this.getModel();
        }
        @Override
        public RecordQueryWrapper<FeatureTourismResource> getWrapper(OperationTypeEnum type) {
            switch (type){
                case INSERT:
                case UPDATE:
                    return new RecordQueryWrapper<FeatureTourismResource>().select(FeatureTourismResource::getResourceName,FeatureTourismResource::getResourceTypeName,
                            FeatureTourismResource::getRegionalName,FeatureTourismResource::getUpdateName,FeatureTourismResource::getAddressDetail,FeatureTourismResource::getUpdateTime);
                case DELETE:
                    return new RecordQueryWrapper<FeatureTourismResource>().select(FeatureTourismResource::getResourceName);
                default:
                    throw new BusinessException(BusinessExceptionEnum.USER_OPERATION_ERROR);
            }
        }
    }
    ;

    @Override
    public RecordQueryWrapper getWrapper(OperationTypeEnum operationTypeEnum) {
        return null;
    }

    @Override
    public String getSummary(OperationTypeEnum operationTypeEnum) {
        return null;
    }

    @Override
    public ModelInfo getModelInfo() {
        return null;
    }

    private ModelInfo model;

    public ModelInfo getModel() {
        return model;
    }

    OperationEnum(ModelEnum modelEnum) {
        final ModelInfo modelInfo = new ModelInfo();
        modelInfo.setId(modelEnum.getId());
        modelInfo.setName(modelEnum.getModelName());
        this.model = modelInfo;
    }

    @Override
    public String getOperationId() {
        return this.name().toLowerCase();
    }
}
