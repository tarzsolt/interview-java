package hu.bca.library.services.impl;

import hu.bca.library.clients.OpenLibraryApiClient;
import hu.bca.library.models.Book;
import hu.bca.library.services.OpenLibraryService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class OpenLibraryServiceImpl implements OpenLibraryService {

    private final OpenLibraryApiClient apiClient;

    public OpenLibraryServiceImpl(OpenLibraryApiClient apiClient) {
        this.apiClient = apiClient;
    }

    @Override
    public Optional<String> getBookByWorkId(String workId) {
        return apiClient.getFirstPublishDateByWorkId(workId);
    }

    @Override
    public Integer extractYearFromPublicationDate(String firstPublishDate) {
        Integer result = null;
        if (firstPublishDate != null) {
            if (firstPublishDate.length() == 4) {
                result = Integer.parseInt(firstPublishDate);
            } else {
                result = Integer.parseInt(firstPublishDate.substring(firstPublishDate.length()-4));
            }
        }
        return result;
    }

    @Override
    public HashMap<String, String> mapWorkIdToPublicationYear(List<Book> books) {
        HashMap<String, String> workIdPublicationYearMap = new HashMap<>();
        books.stream()
                .map(Book::getWorkId)
                .forEach(workId -> workIdPublicationYearMap.put(
                        workId,
                        getBookByWorkId(workId).orElse(null)
                ));
        return workIdPublicationYearMap;
    }
}
