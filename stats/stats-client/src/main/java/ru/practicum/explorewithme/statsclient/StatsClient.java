package ru.practicum.explorewithme.statsclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.explorewithme.statsdto.HitDto;

import java.util.Map;

public class StatsClient {
    private final RestTemplate rest;

    @Value("${stats-server.url}")
    private String serverUrl;

    private static final String HIT_API_PREFIX = "/hit";
    private static final String STATS_API_PREFIX = "/stats";

    public StatsClient(RestTemplate rest) {
        this.rest = rest;
    }

    public void addHit(HitDto hitDto) {
        HttpEntity<HitDto> requestEntity = new HttpEntity<>(hitDto);
        rest.exchange(serverUrl + HIT_API_PREFIX, HttpMethod.POST, requestEntity, Object.class);
    }

    public ResponseEntity<Object> getStats(String start, String end, String[] uris, boolean unique) {
        Map<String, Object> parameters;
        String url;

        if (uris != null) {
            parameters = Map.of(
                    "start", start,
                    "end", end,
                    "uris", uris,
                    "unique", unique);
            url = serverUrl + STATS_API_PREFIX + "/?start={start}&end={end}&uris={uris}&unique={unique}";
        } else {
            parameters = Map.of(
                    "start", start,
                    "end", end,
                    "unique", unique);
            url = serverUrl + STATS_API_PREFIX + "/?start={start}&end={end}&unique={unique}";
        }

        ResponseEntity<Object> response = rest.getForEntity(url, Object.class, parameters);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }

        return responseBuilder.build();
    }
}
