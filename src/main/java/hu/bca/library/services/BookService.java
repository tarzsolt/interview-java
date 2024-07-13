package hu.bca.library.services;

import hu.bca.library.models.Book;

import java.util.List;

public interface BookService {
    Book addAuthor(Long bookId, Long authorId);

    void updateAllWithYear();

    List<Book> getBooksByAuthorOrigin(String authorOrigin, Integer from);
}
