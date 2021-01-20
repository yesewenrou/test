package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import net.cdsunrise.common.utility.AssertUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.req.*;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums.MaterialTypeEnum;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.exception.ParamErrorException;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * MaterialCheckUtil
 * 物资校验工具类
 * @author LiuYin
 * @date 2021/1/17 16:22
 */
public class MaterialCheckUtil {


    /**
     * 校验保存物资条目
     * @param request 物资条目请求
     */
    public static void checkSaveTerm(MaterialTermRequest request){
        AssertUtil.notNull(request, () -> new ParamErrorException("请求为空"));

        checkAndSetAfterTrim(request::getName, request::setName, 1,100,"物资名称");
        checkAndSetAfterTrim(request::getPurpose, request::setPurpose, 1,100,"物资用途");
        checkAndSetAfterTrim(request::getUnit, request::setUnit, 1,50,"统计单位");

        // 期初库存必须是大于0
        checkPositiveNumber(request.getBeginningInventory(), "期初库存");
        // 物资类型校验
        checkType(request.getType());

        // 图片地址校验
        final List<String> pics = request.getPics();
        if(CollectionUtils.isNotEmpty(pics)){
            request.setPics(checkAndTrimPics(pics));
        }
    }

    /**
     * 校验修改物资条目
     * @param request 物资条目请求
     */
    public static void checkUpdateTerm(MaterialTermRequest request){
        checkSaveTerm(request);
        AssertUtil.notNull(request.getId(), () -> new ParamErrorException("id为空"));
    }


    /**
     * 校验采购保存请求
     * @param request
     */
    public static void checkSavePurchase(MaterialPurchaseRequest request) {
        AssertUtil.notNull(request, () -> new ParamErrorException("采购请求为空"));
        // 采购数量为正数
        checkPositiveNumber(request.getQuantity(), "采购数量");
        // 采购日期不能为空
        AssertUtil.notNull(request.getPurchaseDate(), () -> new ParamErrorException("采购日期为空"));

        // 如果单号不为空，则要校验长度
        final String purchaseCode = request.getPurchaseCode();
        if(!StringUtils.isEmpty(purchaseCode)){
            checkAndSetAfterTrim(request::getPurchaseCode, request::setPurchaseCode, 0, 200, "采购单号");
        }

        // 如果采购人员不为空，则要校验长度
        final String purchaseUser = request.getPurchaseUser();
        if(!StringUtils.isEmpty(purchaseUser)){
            checkAndSetAfterTrim(request::getPurchaseUser,request::setPurchaseUser, 0, 50, "采购人员");
        }

        // 如果采购凭证不为空，则要校验长度
        final String voucherUrl = request.getVoucherUrl();
        if(!StringUtils.isEmpty(voucherUrl)){
            checkAndSetAfterTrim(request::getVoucherUrl, request::setVoucherUrl, 0, 200, "采购凭证");
        }
    }

    /**
     * 校验报损保存请求
     * @param request 报损请求
     */
    public static void checkSaveLossReport(MaterialLossReportRequest request) {
        AssertUtil.notNull(request, () -> new ParamErrorException("报损请求为空"));
        // 报损数量为正数
        checkPositiveNumber(request.getQuantity(), "报损数量");
        // 报损日期不能为空
        AssertUtil.notNull(request.getReportDate(), () -> new ParamErrorException("报损日期为空"));

        // 如果单号不为空，则要校验长度
        final String reportCode = request.getReportCode();
        if(!StringUtils.isEmpty(reportCode)){
            checkAndSetAfterTrim(request::getReportCode, request::setReportCode, 0, 200, "报损单号");
        }

        // 如果报损人员不为空，则要校验长度
        final String reportUser = request.getReportUser();
        if(!StringUtils.isEmpty(reportUser)){
            checkAndSetAfterTrim(request::getReportUser,request::setReportUser, 0, 50, "报损人员");
        }

        // 如果报损凭证不为空，则要校验长度
        final String voucherUrl = request.getVoucherUrl();
        if(!StringUtils.isEmpty(voucherUrl)){
            checkAndSetAfterTrim(request::getVoucherUrl, request::setVoucherUrl, 0, 200, "报损凭证");
        }
    }

    /**
     * 校验核算保存请求
     * @param request 核算请求
     */
    public static void checkSaveAccounting(MaterialAccountingRequest request) {
        AssertUtil.notNull(request, () -> new ParamErrorException("核算请求为空"));

        // 核算数量不能为空，也不能为0
        final Long quantity = request.getQuantity();
        AssertUtil.notNull(quantity, () -> new ParamErrorException("核校数量为空"));
        AssertUtil.isTrue(quantity != 0L, () -> new ParamErrorException("核校数量为0"));

        // 核算日期不能为空
        AssertUtil.notNull(request.getAccountingDate(), () -> new ParamErrorException("核算日期为空"));

        // 如果单号不为空，则要校验长度
        final String accountingCode = request.getAccountingCode();
        if(!StringUtils.isEmpty(accountingCode)){
            checkAndSetAfterTrim(request::getAccountingCode, request::setAccountingCode, 0, 200, "核算单号");
        }

        // 如果核算人员不为空，则要校验长度
        final String accountingUser = request.getAccountingUser();
        if(!StringUtils.isEmpty(accountingUser)){
            checkAndSetAfterTrim(request::getAccountingUser,request::setAccountingUser, 0, 50, "核算人员");
        }

        // 如果核算凭证不为空，则要校验长度
        final String voucherUrl = request.getVoucherUrl();
        if(!StringUtils.isEmpty(voucherUrl)){
            checkAndSetAfterTrim(request::getVoucherUrl, request::setVoucherUrl, 0, 200, "核算凭证");
        }
    }

    /**
     * 校验租赁保存请求
     * @param request 租赁请求
     */
    public static void checkSaveLease(MaterialLeaseRequest request) {
        AssertUtil.notNull(request, () -> new ParamErrorException("租赁请求为空"));

        // 租赁数量不能为空，也不能为0
        final Long quantity = request.getQuantity();
        AssertUtil.notNull(quantity, () -> new ParamErrorException("租赁数量为空"));
        AssertUtil.isTrue(quantity != 0L, () -> new ParamErrorException("租赁数量为0"));

        // 核算日期不能为空
        AssertUtil.notNull(request.getLeaseDate(), () -> new ParamErrorException("租赁日期为空"));

        // 如果单号不为空，则要校验长度
        final String accountingCode = request.getLeaseCode();
        if(!StringUtils.isEmpty(accountingCode)){
            checkAndSetAfterTrim(request::getLeaseCode, request::setLeaseCode, 0, 200, "租赁单号");
        }

        // 如果核算人员不为空，则要校验长度
        final String accountingUser = request.getLeaseUser();
        if(!StringUtils.isEmpty(accountingUser)){
            checkAndSetAfterTrim(request::getLeaseUser,request::setLeaseUser, 0, 50, "租赁人员");
        }

        // 如果核算凭证不为空，则要校验长度
        final String voucherUrl = request.getVoucherUrl();
        if(!StringUtils.isEmpty(voucherUrl)){
            checkAndSetAfterTrim(request::getVoucherUrl, request::setVoucherUrl, 0, 200, "租赁凭证");
        }
    }




    private static List<String> checkAndTrimPics(List<String> pics){
        if(CollectionUtils.isEmpty(pics)){
            return new ArrayList<>(0);
        }
        final List<String> list = new ArrayList<>(pics.size());
        pics.forEach(s -> {
            checkEmpty(s, "图片地址");
            checkLength(s, 1, 200, "图片地址");
            final String trim = s.trim();
            checkEmpty(trim, "图片地址");
            checkLength(trim, 1, 200, "图片地址");
            list.add(trim);
        });
        return list;
    }



    /**
     * 校验物资类型
     * @param type 物资类型
     */
    private static void checkType(Integer type){
        AssertUtil.notNull(type, () -> new ParamErrorException("物资类型为空"));
        AssertUtil.isTrue(MaterialTypeEnum.isExist(type), () -> new ParamErrorException("物资类型非法"));
    }


    /**
     * 自然数校验
     * @param number 数值
     * @param name 字段名称
     */
    private static void checkNatureNumber(Long number, String name){
        AssertUtil.notNull(number, () -> new RuntimeException(name +"为空"));
        AssertUtil.isTrue(number > 0, () -> new RuntimeException(name + "不能是负数"));

    }

    /**
     * 正数校验
     * @param number 数值
     * @param name 字段名称
     */
    private static void checkPositiveNumber(Long number, String name){
        AssertUtil.notNull(number, () -> new ParamErrorException(name +"为空"));
        AssertUtil.isTrue(number > 0, () -> new ParamErrorException(name + "必须是正数"));
    }


    /**
     * 校验并设置成trim后的值
     * @param getter 获取字符串的方法
     * @param setter 设置字符串的方法
     * @param minLength 最小长度
     * @param maxLength 最大长度
     * @param name 字段名称
     */
    private static void checkAndSetAfterTrim(Supplier<String> getter, Consumer<String> setter, int minLength, int maxLength, String name){
        final String str = getter.get();
        checkEmpty(str, name);
        checkLength(str, minLength, maxLength, name);

        final String trim = str.trim();
        checkEmpty(trim, name);
        checkLength(trim, minLength, maxLength, name);

        setter.accept(trim);
    }

    /**
     * 校验空
     * @param s 字符串
     * @param name 字段名称
     */
    private static void checkEmpty(String s, String name){
        AssertUtil.notEmpty(s, () -> new ParamErrorException(name + "为空"));
    }

    /**
     * 校验长度
     * @param s 字符串
     * @param min 最小长度
     * @param max 最大长度
     * @param name 字段名称
     */
    private static void checkLength(String s, int min, int max, String name){
        Objects.requireNonNull(s);
        final int length = s.length();

        AssertUtil.isTrue(length <= max, () -> new ParamErrorException(name + "长度过大"));
        AssertUtil.isTrue(length >= min, () -> new ParamErrorException(name + "长度过小"));

    }


    /**
     * 校验物资条目的分页查询
     * @param pageRequest request
     */
    public static void checkPageTerm(MaterialTermPageRequest pageRequest) {
        AssertUtil.notNull(pageRequest , () -> new ParamErrorException("查询请求为空"));

        final Integer current = pageRequest.getCurrent();
        AssertUtil.notNull(current, () -> new ParamErrorException("当前页为空"));
        AssertUtil.isTrue(current > 0, () -> new ParamErrorException("当前页错误"));

        final Integer size = pageRequest.getSize();
        AssertUtil.notNull(size, () -> new ParamErrorException("每页条数为空"));
        AssertUtil.isTrue(size < 1000, () -> new ParamErrorException("每页条数过大"));

        final String name = pageRequest.getName();
        if(Objects.nonNull(name)){
            pageRequest.setName(name.trim());
        }

        final Integer type = pageRequest.getType();
        if(Objects.nonNull(type)){
            AssertUtil.isTrue(MaterialTypeEnum.isExist(type), () -> new ParamErrorException("非法的物资类型"));
        }

    }
}
