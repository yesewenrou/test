package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lijiafeng
 * @date 2020/3/4 0:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeyAndValue<K, V> {
    private K key;
    private V value;
}
