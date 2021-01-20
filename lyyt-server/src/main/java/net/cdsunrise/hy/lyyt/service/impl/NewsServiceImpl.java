package net.cdsunrise.hy.lyyt.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.common.utility.utils.JsonUtils;
import net.cdsunrise.hy.lyyt.entity.vo.NewsContentVO;
import net.cdsunrise.hy.lyyt.service.NewsService;
import net.cdsunrise.hy.lyyt.utils.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.List;

/**
 * @author YQ on 2020/3/7.
 */
@Service
@Slf4j
public class NewsServiceImpl implements NewsService {
    @Value("${hy.news.url}")
    private String newsAddress;
    @Value("${hy.news.forward-url}")
    private String forwardUrl;

    private RestClient restClient;

    private static final String LIST_URL = "/open/news/newsList?limit=LIMIT_PARAM&page=PAGE_PARAM&typeCode=TYPE_CODE_PARAM";

    private final RestTemplateBuilder restTemplateBuilder;

    private static final Long UPDATE_INTERVAL = 30 * 1000L;

    private Long lastUpdateTime;

    private List<NewsContentVO> cacheData;


    @Autowired
    public NewsServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplateBuilder = restTemplateBuilder;
    }


    @PostConstruct
    public void init() {
        RestTemplate restTemplate = restTemplateBuilder
                .setConnectTimeout(Duration.ofMillis(20 * 1000))
                .setReadTimeout(Duration.ofMillis(40 * 1000))
                .build();
        restClient = new RestClient(newsAddress, restTemplate);
    }

    @Override
    public List<NewsContentVO> list() {
        String limit = "20";
        String page= "1";
        String typeCode = "";

        long now = System.currentTimeMillis();
        if (lastUpdateTime != null && (now - lastUpdateTime) < UPDATE_INTERVAL) {
            log.info("list cache={}", JsonUtils.toJsonString(cacheData));
            return cacheData;
        }
        try {

            String url = LIST_URL
                    .replace("LIMIT_PARAM", limit)
                    .replace("PAGE_PARAM", page)
                    .replace("TYPE_CODE_PARAM", typeCode);
            String resp = restClient.get(url);
            log.info("list resp = {}", resp);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(resp).get("data").get("content");
            List<NewsContentVO> data = objectMapper.readValue(jsonNode.toString(), new TypeReference<List<NewsContentVO>>() {
            });
            data.forEach(e-> e.setForwardUrl(forwardUrl+e.getId()));
            lastUpdateTime=now;
            cacheData = data;
            log.info("list update={}", JsonUtils.toJsonString(cacheData));
            return data;
        } catch (Exception e) {
            log.error("list error=", e);
        }
        return null;
    }
}
