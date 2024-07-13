package hu.bca.library.services;

import hu.bca.library.models.Book;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface OpenLibraryService {
    Optional<String> getBookByWorkId(String workId);

    Integer extractYearFromPublicationDate(String firstPublishDate);

    HashMap<String, String> mapWorkIdToPublicationYear(List<Book> books);
}
