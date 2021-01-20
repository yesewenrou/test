package net.cdsunrise.hy.lyyt.service;

import net.cdsunrise.hy.lyyt.entity.resp.MerchantTypeCountResponse;

import java.util.List;

/**
 * @author sh
 * @date 2020-01-18 16:33
 */
public interface MerchantTypeCountService {
    List<MerchantTypeCountResponse> query();
}
