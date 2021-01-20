package net.cdsunrise.hy.lyyt.controller;

import lombok.AllArgsConstructor;
import net.cdsunrise.hy.lyyt.annatation.DataType;
import net.cdsunrise.hy.lyyt.entity.vo.NewsContentVO;
import net.cdsunrise.hy.lyyt.enums.DataTypeEnum;
import net.cdsunrise.hy.lyyt.service.NewsService;
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
    @DataType({DataTypeEnum.LYZXSJ})
    public List<NewsContentVO> list() {
        return newsService.list();
    }
}
