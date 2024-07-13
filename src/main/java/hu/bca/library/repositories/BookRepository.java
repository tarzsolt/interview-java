package hu.bca.library.repositories;

import hu.bca.library.models.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, Long> {

    @Query("SELECT b FROM Book b JOIN b.authors a WHERE a.country = :country " +
            "AND (:year IS NULL OR b.year >= :year) ORDER BY b.year ASC")
    List<Book> findBooksByAuthorCountryAndYear(@Param("country") String country,
                                               @Param("year") Integer year);
}
