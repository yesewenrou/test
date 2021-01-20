package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.helper;

import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.common.utility.AssertUtil;
import net.cdsunrise.common.utility.utils.JsonUtils;
import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.DataDictionaryFeignClient;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.vo.DataDictionaryVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.dto.SimpleDataDictionaryDto;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums.DictionaryCodeEnum;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.exception.DictionaryException;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.exception.enums.DictionaryExceptionEnum;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: suzhouhe @Date: 2019/12/20 15:16 @Description:
 */
@Slf4j
public class DataDictionaryHelper {

    public static DataDictionaryFeignClient dataDictionaryFeignClient;

    private static final String DEFAULT_CHAR = "";

    private static final Map<String, SimpleDataDictionaryDto> DICTIONARY_MAP = new HashMap<>();

    private static final Map<String, List<SimpleDataDictionaryDto>> DICTIONARY_LIST_MAP = new HashMap<>();

    private static final Map<DictionaryCodeEnum, Map<String, SimpleDataDictionaryDto>> ENUM_MAP = new HashMap<>();

    private static final String[] LOAD_CODE_ARRAY = Arrays.stream(DictionaryCodeEnum.values()).map(DictionaryCodeEnum::code).toArray(String[]::new);


    /**
     * 根据枚举类型获取字典列表
     *
     * @param codeEnum 父类型枚举
     * @return 字典列表
     */
    public static List<SimpleDataDictionaryDto> getListByEnum(DictionaryCodeEnum codeEnum) {
        Objects.requireNonNull(codeEnum, "dictionary code enum is null");
        return getListByParentCode(codeEnum.code());
    }

    /**
     * 根据父类型code及子类型名称，获取code
     * @param codeEnum 父类型
     * @param name 子类型名称
     * @return 子类型code
     */
    public static String getCodeByName(DictionaryCodeEnum codeEnum, String name){
        Objects.requireNonNull(name, "name is null");
        final Map<String, SimpleDataDictionaryDto> dtoMap = ENUM_MAP.get(codeEnum);
        if(Objects.isNull(dtoMap)){
            final List<SimpleDataDictionaryDto> listByEnum = getListByEnum(codeEnum);
            ENUM_MAP.put(codeEnum, listByEnum.stream().collect(Collectors.toMap(SimpleDataDictionaryDto::getName, Function.identity())));
            return ENUM_MAP.get(codeEnum).get(name).getCode();
        }else{
            return dtoMap.get(name).getCode();
        }
    }



    /**
     * 根据父类型获取子类型
     *
     * @param code 父类型code
     * @return 字典列表
     */
    private static List<SimpleDataDictionaryDto> getListByParentCode(String code) {
        AssertUtil.notNull(code, DictionaryException.fail(DictionaryExceptionEnum.PARENT_CODE_IS_NULL));
        final List<SimpleDataDictionaryDto> simpleDataDictionaryDtos = getDictionaryListMap().get(code);
        if (Objects.isNull(simpleDataDictionaryDtos)) {
            final List<SimpleDataDictionaryDto> list = getDictionaryMap().values().stream().filter(dto -> code.equals(dto.getParentCode())).collect(Collectors.toList());
            getDictionaryListMap().put(code, list);
            return list;
        } else {
            return simpleDataDictionaryDtos;
        }


    }


    public static Map<String, List<SimpleDataDictionaryDto>> getDictionaryListMap() {
        return DICTIONARY_LIST_MAP;
    }

    public static String[] getLoadCodeArray() {
        return LOAD_CODE_ARRAY;
    }

    public static DataDictionaryFeignClient getDataDictionaryFeignClient() {
        return dataDictionaryFeignClient;
    }

    /**
     * 校验数据字典
     *
     * @param codeEnum 数据字典根分类枚举
     * @param code     数据字典code
     */
    public static void check(DictionaryCodeEnum codeEnum, String code) {
        final SimpleDataDictionaryDto simpleDataDictionaryDto = getDictionaryMap().get(code);
        AssertUtil.notNull(simpleDataDictionaryDto, DictionaryException.fail(DictionaryExceptionEnum.DICTIONARY_CODE_NOT_FOUND));
        AssertUtil.isTrue(simpleDataDictionaryDto.getParentCode().equals(codeEnum.code()), DictionaryException.fail(DictionaryExceptionEnum.ILLEGAL_DICTIONARY_CODE));
    }


    /**
     * 更新数据字典
     */
    public static void update() {
        final Result<Map<String, DataDictionaryVO>> result = getDataDictionaryFeignClient().getByCodes(LOAD_CODE_ARRAY);
        log.info("data dictionary data:{}", JsonUtils.toJsonString(result));
        final Map<String, DataDictionaryVO> map = result.getData();
        if (!CollectionUtils.isEmpty(map)) {
            map.values().forEach(DataDictionaryHelper::addToMap);
        }
    }

    private static void addToMap(DataDictionaryVO vo) {
        final List<DataDictionaryVO> children = vo.getChildren();
        if (!CollectionUtils.isEmpty(children)) {
            children.forEach(DataDictionaryHelper::addToMap);
        } else {
            final SimpleDataDictionaryDto dto = SimpleDataDictionaryDto.from(vo);
            final String code = dto.getCode();
            getDictionaryMap().put(code, dto);
        }
    }

    public static Map<String, SimpleDataDictionaryDto> getDictionaryMap() {
        return DICTIONARY_MAP;
    }

    public static String getNameByCode(String code) {
        AssertUtil.notEmpty(code, () -> new DictionaryException(DictionaryExceptionEnum.ILLEGAL_DICTIONARY_CODE));
        SimpleDataDictionaryDto simpleDataDictionaryDto = getDictionaryMap().get(code);

        return Objects.nonNull(simpleDataDictionaryDto) ? simpleDataDictionaryDto.getName() : "";
    }

}
