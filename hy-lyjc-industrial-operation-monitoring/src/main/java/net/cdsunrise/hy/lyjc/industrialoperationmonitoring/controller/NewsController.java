package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.controller;

import lombok.AllArgsConstructor;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.vo.NewsContentVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.NewsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author YQ on 2020/3/7.
 */
@RestController
@RequestMapping("/news")
@AllArgsConstructor
public class NewsController {
    private final NewsService newsService;

    @GetMapping
    public List<NewsContentVO> list() {
        return newsService.list();
    }
}
