package com.reinke.betterreadswebapp.book;

import com.reinke.betterreadswebapp.bookuser.UserBook;
import com.reinke.betterreadswebapp.bookuser.UserBookPrimaryKey;
import com.reinke.betterreadswebapp.bookuser.UserBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserBookRepository userBookRepository;


    private final String BOOK_COVER_ROOT = "https://covers.openlibrary.org/b/id/";

    @GetMapping("/books/{bookId}")
    public String getBook(@PathVariable String bookId, Model model, @AuthenticationPrincipal OAuth2User oAuth2User) {
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

            // check if user is logged in and get information about relationship that user have with this book
            // get UserBook from repository and put it to the model
            String userId = oAuth2User.getAttribute("login");
            if (userId != null) {
                model.addAttribute("loggedIn", true);
                model.addAttribute("bookId", bookId);

                UserBookPrimaryKey key = new UserBookPrimaryKey();
                key.setUserId(userId);
                key.setBookId(bookId);
                Optional<UserBook> optionalUserBook = userBookRepository.findById(key);

                UserBook userBook = null;
                boolean isUserBookPresent = false;

                if (optionalUserBook.isPresent()) {
                    isUserBookPresent = true;
                    userBook = optionalUserBook.get();
                    model.addAttribute("userBook", userBook);
                }

                model.addAttribute("isUserBookPresent", isUserBookPresent);


            }



            return "book";
        }
        return "book-not-found";
    }


}
