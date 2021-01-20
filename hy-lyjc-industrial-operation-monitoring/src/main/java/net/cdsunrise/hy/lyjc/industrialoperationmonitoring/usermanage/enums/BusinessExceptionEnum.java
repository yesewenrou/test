package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.enums;

import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.exception.BusinessException;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author YQ
 */
public enum BusinessExceptionEnum {

    /**
     * 处理成功
     */
    SUCCESS("08000000", "success", "操作成功"),
    /**
     * 系统错误
     */
    FAILED("08000001", "failed", "系统错误"),
    PARAM_ERROR("08000003", "param error", "参数错误"),
    NAME_EXIST("08000004", "name exist", "名称已存在"),
    ROLE_DELETE_ERROR("08000005", "role links", "角色有关联的用户"),
    NO_PERMISSION("08000006", "no permission", "无权访问"),
    MENU_CODE_EXIST("08000007", "code exist", "菜单code已存在"),
    USER_OPERATION_ERROR("01000008", "unSupported","不支持的用户操作类型"),
    ATTACHMENT_SAVE_FAILED("08000008", "attachment save failed","附件保存失败" ),
    MATERIAL_NAME_EXISTS("08000009","material name exists" ,"物资名称已存在" ),
    NOT_FOUND_RECORD("08000010","not found record" ,"记录未找到" ),
    MATERIAL_TERM_NAME_EXISTS("08000011","material term name exists" ,"物资条目名称已经存在" ),
    MATERIAL_TERM_NOT_FOUND("08000012","material term not found" , "物资条目没有找到");


    private String code;
    private String message;
    private String chineseMessage;

    private static final Map<String, BusinessExceptionEnum> CODE_MAP =
            Collections.unmodifiableMap(Arrays.stream(BusinessExceptionEnum.values()).collect(Collectors.toMap(BusinessExceptionEnum::getCode, Function.identity())));


    BusinessExceptionEnum(String code, String message, String chineseMessage) {
        this.code = code;
        this.message = message;
        this.chineseMessage = chineseMessage;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getChineseMessage() {
        return chineseMessage;
    }

    public static Map<String, BusinessExceptionEnum> getCodeMap() {
        return CODE_MAP;
    }

    public Supplier<BusinessException> fail(){
        return BusinessException.fail(this);
    }
}
