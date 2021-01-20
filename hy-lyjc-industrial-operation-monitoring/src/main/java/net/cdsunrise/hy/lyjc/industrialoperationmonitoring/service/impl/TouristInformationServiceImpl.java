package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.TouristInformationService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.time.Duration;

/**
 * @author YQ on 2020/3/24.
 */
@Service
@Slf4j
public class TouristInformationServiceImpl implements TouristInformationService {
    @Value("${hy.news.url}")
    private String newsAddress;

    private static final String STATISTICS_MONTH_URI = "/open/news/inRecentMonth";
    private static final String STATISTICS_YEAR_URI = "/open/news/inRecentYear";
    private static ObjectMapper mapper = new ObjectMapper();

    private final RestTemplateBuilder restTemplateBuilder;

    private RestClient restClient;

    @Autowired
    public TouristInformationServiceImpl(RestTemplateBuilder restTemplateBuilder) {
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
    public Object statisticsMonth() {
        return statistics(STATISTICS_MONTH_URI);
    }

    @Override
    public Object statisticsYear() {
        return statistics(STATISTICS_YEAR_URI);
    }


    public Object statistics(String uri) {
        try {
            String data = restClient.get(uri);
            return mapper.readTree(data).get("data");
        } catch (Exception e) {
            log.error("statisticsMonth error:", e);
        }
        return null;
    }
}
