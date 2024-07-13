package hu.bca.library.controllers;

import hu.bca.library.models.Book;
import hu.bca.library.services.BookService;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RepositoryRestController("books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @ResponseStatus(HttpStatus.CREATED)

    @RequestMapping("/{bookId}/add_author/{authorId}")
    @ResponseBody Book addAuthor(@PathVariable Long bookId, @PathVariable Long authorId) {
        return this.bookService.addAuthor(bookId, authorId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/update-all-with-year")
    public void updateAllWithYear() {
        bookService.updateAllWithYear();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping("/query/{authorOrigin}")
    @ResponseBody
    public List<Book> getBooksByAuthorOrigin(@PathVariable String authorOrigin,
                                             @RequestParam(required = false) Integer from) {
        return bookService.getBooksByAuthorOrigin(authorOrigin, from);
    }
}
