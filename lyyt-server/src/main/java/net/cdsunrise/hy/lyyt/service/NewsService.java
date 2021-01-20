package net.cdsunrise.hy.lyyt.service;

import net.cdsunrise.hy.lyyt.entity.vo.NewsContentVO;

import java.util.List;

/**
 * @author YQ on 2020/3/7.
 */
public interface NewsService {
    List<NewsContentVO> list();
}
