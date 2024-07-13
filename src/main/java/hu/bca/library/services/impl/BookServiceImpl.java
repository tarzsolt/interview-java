package hu.bca.library.services.impl;

import hu.bca.library.models.Author;
import hu.bca.library.models.Book;
import hu.bca.library.repositories.AuthorRepository;
import hu.bca.library.repositories.BookRepository;
import hu.bca.library.services.BookService;
import hu.bca.library.services.OpenLibraryService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final OpenLibraryService openLibraryService;

    public BookServiceImpl(BookRepository bookRepository,
                           AuthorRepository authorRepository,
                           OpenLibraryService openLibraryService) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.openLibraryService = openLibraryService;
    }

    @Override
    public Book addAuthor(Long bookId, Long authorId) {
        Optional<Book> book = this.bookRepository.findById(bookId);
        if (book.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Book with id %s not found", bookId));
        }
        Optional<Author> author = this.authorRepository.findById(authorId);
        if (author.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Author with id %s not found", authorId));
        }

        List<Author> authors = book.get().getAuthors();
        authors.add(author.get());

        book.get().setAuthors(authors);
        return this.bookRepository.save(book.get());
    }

    @Override
    public void updateAllWithYear() {
        List<Book> books = StreamSupport
                .stream(bookRepository.findAll().spliterator(), false)
                .toList();

        HashMap<String, String> workIdPublicationYearMap = openLibraryService.mapWorkIdToPublicationYear(books);

        books.forEach(book -> {
            book.setYear(openLibraryService.extractYearFromPublicationDate(
                    workIdPublicationYearMap.get(book.getWorkId())));
            bookRepository.save(book);
        });
    }

    @Override
    public List<Book> getBooksByAuthorOrigin(String authorOrigin, Integer from) {
        return bookRepository.findBooksByAuthorCountryAndYear(authorOrigin, from);
    }
}
