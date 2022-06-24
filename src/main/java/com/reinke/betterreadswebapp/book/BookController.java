package com.reinke.betterreadswebapp.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    private final String BOOK_COVER_ROOT = "https://covers.openlibrary.org/b/id/";

    @GetMapping("/books/{bookId}")
    public String getBook(@PathVariable String bookId, Model model) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            String coverId;
            if (book.getCoverId() != null && book.getCoverId().size() > 0) {
                coverId = BOOK_COVER_ROOT + book.getCoverId().get(0) + "-L.jpg";
            } else {
                coverId = "/images/no-image.png";
            }

            model.addAttribute("cover", coverId);
            model.addAttribute("book", book);

            return "book";
        }
        return "book-not-found";
    }


}
