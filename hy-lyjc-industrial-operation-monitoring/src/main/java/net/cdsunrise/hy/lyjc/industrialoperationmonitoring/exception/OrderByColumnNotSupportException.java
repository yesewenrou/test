package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.exception;

/**
 * 不支持的排序字段
 *
 * @author lijiafeng
 * @date 2019/6/28 13:32
 */
public class OrderByColumnNotSupportException extends RuntimeException {

    public OrderByColumnNotSupportException(String message) {
        super(message);
    }
}
