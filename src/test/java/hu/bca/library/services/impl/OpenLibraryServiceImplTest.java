package hu.bca.library.services.impl;

import hu.bca.library.clients.OpenLibraryApiClient;
import hu.bca.library.models.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OpenLibraryServiceImplTest {

    @Mock
    private OpenLibraryApiClient apiClient;

    @InjectMocks
    private OpenLibraryServiceImpl service;

    @Test
    void should_ReturnNull_when_PublicationDateIsNull() {
        assertNull(service.extractYearFromPublicationDate(null));
    }

    @Test
    void should_ExtractYear_when_UsingFullDateFormatPublicationDate() {
        assertEquals(2007, service.extractYearFromPublicationDate("November 27, 2007"));
    }

    @Test
    void should_ExtractYear_when_UsingMediumDateFormatPublicationDate() {
        assertEquals(1992, service.extractYearFromPublicationDate("August 1992"));
    }

    @Test
    void should_ExtractYear_when_UsingShortDateFormatPublicationDate() {
        assertEquals(1984, service.extractYearFromPublicationDate("1984"));
    }

    @Test
    void mapWorkIdToPublicationYear() {
        var b1 = new Book();
        b1.setWorkId("1");
        var b2 = new Book();
        b2.setWorkId("2");
        var b3 = new Book();
        b3.setWorkId("3");

        when(apiClient.getFirstPublishDateByWorkId(anyString()))
                .thenReturn(Optional.of("November 27, 2007"))
                .thenReturn(Optional.of("1984"))
                .thenReturn(Optional.empty());

        var result = service.mapWorkIdToPublicationYear(List.of(b1, b2, b3));

        assertFalse(result.isEmpty());
        assertEquals("November 27, 2007", result.get("1"));
        assertEquals("1984", result.get("2"));
        assertNull(result.get("3"));
    }
}