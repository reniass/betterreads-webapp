package com.reinke.betterreadswebapp.home;

import com.reinke.betterreadswebapp.user.BookByUserId;
import com.reinke.betterreadswebapp.user.BookByUserIdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@Controller
class HomeController {

    @Autowired
    private BookByUserIdRepository bookByUserIdRepository;

    @GetMapping("/")
    public String home(Model model, @AuthenticationPrincipal OAuth2User oAuth2User) {

        String userId = oAuth2User.getAttribute("login");
        if (userId != null) {
            List<BookByUserId> booksByUserId = bookByUserIdRepository.findByUserId(userId);
            boolean isAnyBook = false;
            if(booksByUserId.size() > 0) {
                model.addAttribute("booksByUserId", booksByUserId);
                isAnyBook = true;
            }
            model.addAttribute("isAnyBook", isAnyBook);
            return "home";
        }

        return "index";
    }

}
