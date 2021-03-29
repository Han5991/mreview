package org.zerock.mreview.repository;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.mreview.repository.search.SearchBoardRepositoty;


@SpringBootTest
public class SearchBoardRepositoryTest {
    SearchBoardRepositoty searchBoardRepositoty;

    @Test
    public void testMovieSearch() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("mno").descending().and(Sort.by("title").ascending()));

        Page<Object[]> result = searchBoardRepositoty.searchPage("t","3", pageable);
    }
}
