package com.reinke.betterreadswebapp.bookuser;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.reinke.betterreadswebapp.book.Book;
import com.reinke.betterreadswebapp.book.BookRepository;
import com.reinke.betterreadswebapp.user.BookByUserId;
import com.reinke.betterreadswebapp.user.BookByUserIdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
class UserBookController {

    @Autowired
    private UserBookRepository userBookRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookByUserIdRepository bookByUserIdRepository;

    private final String BOOK_COVER_ROOT = "https://covers.openlibrary.org/b/id/";


    @PostMapping("/addUserBook")
    public ModelAndView addUserBook(@AuthenticationPrincipal OAuth2User oAuth2User,
                              @RequestBody MultiValueMap<String, String> formData,
                              Model model) {

        String userId = oAuth2User.getAttribute("login");
        String bookId = formData.getFirst("bookId");
        UserBookPrimaryKey key = new UserBookPrimaryKey();
        key.setUserId(userId);
        key.setBookId(bookId);

        LocalDate startDate =  LocalDate.parse(formData.getFirst("startDate"));
        LocalDate endDate = LocalDate.parse(formData.getFirst("endDate"));
        String readingStatus = formData.getFirst("readingStatus");
        int rating = Integer.parseInt(formData.getFirst("rating"));

        Optional<UserBook> optionalUserBook = userBookRepository.findById(key);
        // check if userbook relationhip exist then update data
        if (optionalUserBook.isPresent()) {
            UserBook userBook = optionalUserBook.get();
            userBook.setStartDate(startDate);
            userBook.setEndDate(endDate);
            userBook.setReadingStatus(readingStatus);
            userBook.setRating(rating);

            List<BookByUserId> booksByUserId = bookByUserIdRepository.findByUserId(userId);
            BookByUserId bookByUserId = booksByUserId.stream()
                    .filter(elem -> elem.getBookId().equals(bookId))
                    .collect(Collectors.toList()).get(0);

            bookByUserId.setStartDate(startDate);
            bookByUserId.setEndDate(endDate);
            bookByUserId.setReadingStatus(readingStatus);
            bookByUserId.setRating(rating);


            // if there is no such a relationship
        } else {
            UserBook userBook = new UserBook();
            userBook.setKey(key);
            userBook.setStartDate(startDate);
            userBook.setEndDate(endDate);
            userBook.setReadingStatus(readingStatus);
            userBook.setRating(rating);

            userBookRepository.save(userBook);

            BookByUserId bookByUserId = new BookByUserId();
            UUID uuids = Uuids.timeBased();
            bookByUserId.setId(uuids.toString());
            bookByUserId.setUserId(userId);
            bookByUserId.setBookId(bookId);
            bookByUserId.setStartDate(startDate);
            bookByUserId.setEndDate(endDate);
            bookByUserId.setReadingStatus(readingStatus);
            bookByUserId.setRating(rating);
            // query for a book for authors name
            Optional<Book> optionalBook = bookRepository.findById(bookId);
            Book book = optionalBook.get();
            bookByUserId.setAuthorsName(book.getAuthorsName());
            // set title
            bookByUserId.setTitle(book.getTitle());
            String coverId;
            if (book.getCoverId() != null && book.getCoverId().size() > 0) {
                coverId = BOOK_COVER_ROOT + book.getCoverId().get(0) + "-L.jpg";
            } else  {
                coverId = "/images/no-image.png";
            }

            bookByUserId.setCoverId(coverId);

            bookByUserIdRepository.save(bookByUserId);
        }

        return new ModelAndView("redirect:/books/" + bookId);
    }
}
