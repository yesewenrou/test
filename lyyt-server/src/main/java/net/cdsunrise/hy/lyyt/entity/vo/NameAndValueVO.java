package net.cdsunrise.hy.lyyt.entity.vo;

import lombok.Data;

/**
 * 名称与值视图对象
 * @author LiuYin 2020/2/4
 */
@Data
public class NameAndValueVO<T> {

    private String name;
    private T value;

    public static <T> NameAndValueVO<T> create(String name, T value){
        final NameAndValueVO<T> vo = new NameAndValueVO<>();
        vo.setName(name);
        vo.setValue(value);
        return vo;
    }
}
