package hu.bca.library.clients;

import java.util.Optional;

public interface OpenLibraryApiClient {

    Optional<String> getFirstPublishDateByWorkId(String workId);
}
