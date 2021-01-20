package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

/**
 * @author YQ
 */
public class RestClient {

    private String server;
    private RestTemplate rest;
    private HttpHeaders headers;
    private HttpStatus status;

    public RestClient() {}

    public RestClient(String server, RestTemplate restTemplate){
        this.server = server;
        this.rest = restTemplate;

        this.headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "*/*");

    }


    public String get(String uri) {
        HttpEntity<String> requestEntity = new HttpEntity<>("", headers);
        ResponseEntity<String> responseEntity = rest.exchange(server + uri, HttpMethod.GET, requestEntity, String.class);
        this.setStatus(responseEntity.getStatusCode());
        return responseEntity.getBody();
    }

    public String post(String uri, String json) {
        HttpEntity<String> requestEntity = new HttpEntity<>(json, headers);
        ResponseEntity<String> responseEntity = rest.exchange(server + uri, HttpMethod.POST, requestEntity, String.class);
        this.setStatus(responseEntity.getStatusCode());
        return responseEntity.getBody();
    }

    public HttpStatus getStatus() {
        return status;
    }

    private void setStatus(HttpStatus status) {
        this.status = status;
    }
}