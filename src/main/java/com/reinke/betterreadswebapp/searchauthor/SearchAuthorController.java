package com.reinke.betterreadswebapp.searchauthor;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
class SearchAuthorController {

    private WebClient firstWebClient;

    private WebClient secondWebClient;

    private final String AUTHOR_COVER_ROOT = "https://covers.openlibrary.org/a/olid/";

    private final String BOOK_COVER_ROOT = "https://covers.openlibrary.org/b/id/";


    public SearchAuthorController(WebClient.Builder webClientBuilder){
        this.firstWebClient = webClientBuilder.exchangeStrategies(ExchangeStrategies.builder()
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(16 * 1024 * 1024)
                ).build())
                .baseUrl("http://openlibrary.org/search.json")
                .build();

        this.secondWebClient = webClientBuilder.exchangeStrategies(ExchangeStrategies.builder()
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(16 * 1024 * 1024)
                ).build())
                .baseUrl("http://openLibrary.org/authors/")
                .build();



    }

    // /searchAuthor?query=author id
    @GetMapping("/searchAuthor")
    public String getSearchResults(@RequestParam("q") String authorName, Model model) {

        Mono<SearchAuthorResult> resultAuthorMono = this.firstWebClient.get()
                .uri("?author={authorName}", authorName)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(SearchAuthorResult.class);

        SearchAuthorResult authorResult = resultAuthorMono.block();
        List<AuthorWorkResult> authorWorksResult = authorResult.getDocs()
                .stream()
                .map(book -> {
                    AuthorWorkResult newBook = book;
                    newBook.setCover_i(BOOK_COVER_ROOT + newBook.getCover_i() + "-M.jpg");
                    newBook.setKey(newBook.getKey().replace("/works/", ""));
                    return newBook;
                })
                .collect(Collectors.toList());

        // take author id from author work result
        String authorId = authorWorksResult.get(0).getAuthor_key().get(0);


        Mono<AuthorBasicInfo> authorBasicInfoMono = this.secondWebClient.get()
                .uri("{authorId}.json", authorId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(AuthorBasicInfo.class);

        AuthorBasicInfo authorBasicInfo = authorBasicInfoMono.block();

        model.addAttribute("authorBasicInfo", authorBasicInfo);

        String authorCoverSource = AUTHOR_COVER_ROOT + authorId + "-L.jpg";
        model.addAttribute("authorCoverSource", authorCoverSource);

        model.addAttribute("authorWorksResult", authorWorksResult);



        return "author";
    }





}


