package com.reinke.betterreadswebapp.searchbook;

import com.reinke.betterreadswebapp.book.BookRepository;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class SearchBookController {

    private WebClient webClient;

    private BookRepository bookRepository;

    private final String BOOK_COVER_ROOT = "https://covers.openlibrary.org/b/id/";

    public SearchBookController(WebClient.Builder webClientBuilder, BookRepository bookRepository) {
        this.webClient = webClientBuilder
                                    .exchangeStrategies(ExchangeStrategies.builder()
                                                                    .codecs(configurer -> configurer
                                                                                            .defaultCodecs()
                                                                                            .maxInMemorySize(16 * 1024 * 1024)
                                                                    ).build())
                                    .baseUrl("http://openlibrary.org/search.json")
                                    .build();
        this.bookRepository = bookRepository;
    }

    @GetMapping("/searchBook")
    public String getSearchResults(@RequestParam("query") String query, Model model) {
        System.out.println("Query: " + query);
        Mono<SearchBookResult> result = this.webClient.get()
                .uri("?q={query}", query)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(SearchBookResult.class);

        SearchBookResult searchResult = result.block();
        List<BookResult> bookResults = searchResult.getDocs();



//        searchResult.getDocs()
//                .stream()
//                .map(bookResult -> {
//                    String bookId = bookResult.getKey().replace("/works/", "");
//                    Optional<Book> optionalBook = bookRepository.findById(bookId);
//                    if (optionalBook.isPresent()) {
//                        Book book = optionalBook.get();
//                        String coverUrl;
//                        if (book.getCoverId() != null && book.getCoverId().size() > 0) {
//                            coverUrl = BOOK_COVER_ROOT + book.getCoverId().get(0) + "-M.jpg";
//                        } else {
//                            coverUrl = "/images/no-image.png";
//                        }
//                        coverUrls.add(coverUrl);
//                        return optionalBook.get();
//                    } else {
//                        return null;
//                    }
//                }).filter(book -> book != null)
//                .collect(Collectors.toList());


//        searchResult.getDocs()
//                .stream()
//                .forEach(bookResult -> {
//                    String bookId = bookResult.getKey().replace("/works/", "");
//                    Optional<Book> optionalBook = bookRepository.findById(bookId);
//                    if (optionalBook.isPresent()) {
//                        Book book = optionalBook.get();
//                        String coverUrl = "/images/no-image.png";;
//                        if (book.getCoverId() != null && book.getCoverId().size() > 0) {
//                            coverUrl = BOOK_COVER_ROOT + book.getCoverId().get(0) + "-M.jpg";
//                        }
//                        coverUrls.add(coverUrl);
//                    }
//                    });
        bookResults = bookResults.stream()
                .map(bookResult -> {
                    // set key must be called on the variable book result
                    bookResult.setKey(bookResult.getKey().replace("/works/", ""));
                    String coverId = bookResult.getCover_i();
                    if (StringUtils.hasText(coverId)) {
                        coverId = BOOK_COVER_ROOT + coverId + "-M.jpg";
                    } else {
                        coverId = "/images/no-image.png";
                    }
                    bookResult.setCover_i(coverId);
                    return bookResult;
                }).collect(Collectors.toList());


        model.addAttribute("bookResults", bookResults);

        return "search";
    }
}
