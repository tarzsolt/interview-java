package hu.bca.library.services.impl;

import hu.bca.library.models.Book;
import hu.bca.library.repositories.BookRepository;
import hu.bca.library.services.OpenLibraryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Captor
    ArgumentCaptor<Book> bookArgumentCaptor;

    @Mock
    private OpenLibraryService openLibraryService;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl service;

    @Test
    void updateAllWithYear() {
        Book b1 = new Book();
        b1.setWorkId("1");
        Book b2 = new Book();
        b2.setWorkId("2");
        List<Book> books = List.of(b1, b2);
        HashMap<String, String> workIdPublicationYearMap = new HashMap<>();
        workIdPublicationYearMap.put(b1.getWorkId(), "1990");
        workIdPublicationYearMap.put(b2.getWorkId(), "2000");

        when(bookRepository.findAll()).thenReturn(books);
        when(openLibraryService.mapWorkIdToPublicationYear(books)).thenReturn((workIdPublicationYearMap));
        when(openLibraryService.extractYearFromPublicationDate("1990"))
                .thenReturn(Integer.parseInt("1990"));
        when(openLibraryService.extractYearFromPublicationDate("2000")
        ).thenReturn(Integer.parseInt("2000"));

        service.updateAllWithYear();

        verify(bookRepository, times(1)).findAll();
        verify(bookRepository, times(2)).save(bookArgumentCaptor.capture());
        verify(openLibraryService, times(1)).mapWorkIdToPublicationYear(anyList());
        verify(openLibraryService, times(2)).extractYearFromPublicationDate(anyString());

        List<Book> savedBooks = bookArgumentCaptor.getAllValues();
        Assertions.assertFalse(savedBooks.isEmpty());
        Assertions.assertEquals(1990, savedBooks.get(0).getYear());
        Assertions.assertEquals("1", savedBooks.get(0).getWorkId());
        Assertions.assertEquals(2000, savedBooks.get(1).getYear());
        Assertions.assertEquals("2", savedBooks.get(1).getWorkId());
    }
}