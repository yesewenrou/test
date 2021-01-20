package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.es.common;

import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.es.index.IndexWrapper;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * IndexWrapperFactory
 * 索引包装工厂类
 * @author LiuYin
 * @date 2020/3/25 10:07
 */
public class IndexWrapperFactory {


    private static final Map<String, IndexWrapper> POJO_MAP = new ConcurrentHashMap<>();


    public static <T> IndexWrapper<T> getWrapper(Class<T> clazz) {
        Objects.requireNonNull(clazz, "class is null");

        final String className = clazz.getName();
        final IndexWrapper indexWrapper = getPojoMap().get(className);
        if(Objects.nonNull(indexWrapper)){
            return (IndexWrapper<T>) indexWrapper;
        }
        final IndexWrapper<T> newWrapper = new IndexWrapper<>(clazz);
        getPojoMap().putIfAbsent(className, newWrapper);
        return (IndexWrapper<T>) getPojoMap().get(className);
    }


    private static Map<String, IndexWrapper> getPojoMap() {
        return POJO_MAP;
    }
}
