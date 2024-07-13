package hu.bca.library.clients;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.response.MockRestResponseCreators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;

@RestClientTest(OpenLibraryApiClient.class)
class OpenLibraryApiClientImplTest {

    @Autowired
    private OpenLibraryApiClient client;

    @Autowired
    private MockRestServiceServer server;

    @Test
    void should_GetFirstPublishDateByWorkId() {
        String json = """
                {
                    "description": "The Hitchhiker's Guide to the Galaxy",
                    "first_publish_date": "March 3, 1984",
                    "key": "/works/OL2163649W"
                }
                """;

        server
                .expect(requestTo("https://openlibrary.org/works/1.json"))
                .andRespond(MockRestResponseCreators.withSuccess(json, MediaType.APPLICATION_JSON));

        String firstPublishDateByWorkId = client.getFirstPublishDateByWorkId("1").orElse(null);
        assertEquals("March 3, 1984", firstPublishDateByWorkId);
    }
}