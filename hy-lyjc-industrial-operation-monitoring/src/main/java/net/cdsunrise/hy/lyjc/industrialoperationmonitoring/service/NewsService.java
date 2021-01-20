package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service;


import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.vo.NewsContentVO;

import java.util.List;

/**
 * @author YQ on 2020/3/7.
 */
public interface NewsService {
    List<NewsContentVO> list();
}
