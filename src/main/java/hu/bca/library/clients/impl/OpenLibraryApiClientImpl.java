package hu.bca.library.clients.impl;

import com.fasterxml.jackson.databind.JsonNode;
import hu.bca.library.clients.OpenLibraryApiClient;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Log4j2
@Component
public class OpenLibraryApiClientImpl implements OpenLibraryApiClient {

    @Value("${api.openlibrary.uri}")
    private String OPEN_LIBRARY_API_URI;

    @Value("${api.openlibrary.work.node.first-publish-date}")
    private String FIRST_PUBLISH_DATE_FIELD_NAME;

    private final RestTemplate restTemplate;

    public OpenLibraryApiClientImpl(RestTemplateBuilder builder) {
        restTemplate = builder.build();
    }

    public Optional<String> getFirstPublishDateByWorkId(String workId) {
        try {
            JsonNode jsonNode = restTemplate
                    .exchange(
                            OPEN_LIBRARY_API_URI,
                            HttpMethod.GET,
                            HttpEntity.EMPTY,
                            JsonNode.class,
                            workId)
                    .getBody();

            if (jsonNode != null && jsonNode.has(FIRST_PUBLISH_DATE_FIELD_NAME)) {
                JsonNode firstPublishDate = jsonNode.get(FIRST_PUBLISH_DATE_FIELD_NAME);
                return firstPublishDate != null ? Optional.of(firstPublishDate.asText()) : Optional.empty();
            }
            return Optional.empty();
        } catch (HttpClientErrorException e) {
            log.warn("HTTP Client error while fetching data with workId={} from Open Library API: {}",
                    workId, e.getMessage());
            return Optional.empty();
        }
    }
}