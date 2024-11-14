package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.dto.BookSearchDto;
import com.example.demo.entity.BookEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.service.BookSearchService;
import com.example.demo.service.CartMethodClass;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("bookSearch")
public class BookSearchController {

    private final BookSearchService bookSearchService;

    @Autowired
    @Qualifier("loginCartService")
    private CartMethodClass loginCartService;

    @Autowired
    @Qualifier("guestCartService")
    private CartMethodClass guestCartService;

    private CartMethodClass cartMethodClass;

    public BookSearchController(BookSearchService bookSearchService) {
        this.bookSearchService = bookSearchService;
    }

    // リクエスト毎にユーザーを確認し、適切なカートサービスを選択
    private void choiceUser(HttpSession session) {
        UserEntity user = (UserEntity) session.getAttribute("user");
        if (user != null) {
            this.cartMethodClass = loginCartService;
        } else {
            this.cartMethodClass = guestCartService;
        }
    }

    @RequestMapping("regist")
    public String regist(HttpSession session, Model model) {
        choiceUser(session); // リクエスト毎にユーザーを判定,インターセプターで判別できる？
        UserEntity user = (UserEntity) session.getAttribute("user");
        model.addAttribute("user", user);
        return "bookSearch/regist";
    }

    @PostMapping("Search")
    public String Search(BookSearchDto bookSearchDto, HttpSession session, Model model) {
        choiceUser(session); // リクエスト毎にユーザーを判定
        List<BookEntity> bookList = bookSearchService.searchBooks(bookSearchDto);
        model.addAttribute("bookList", bookList);
        return regist(session, model);
    }

    @GetMapping("getCart")
    public ResponseEntity<Map<Integer, Map<String, Object>>> getCart(HttpSession session) {
        choiceUser(session); // リクエスト毎にユーザーを判定
        Map<Integer, Map<String, Object>> cartList = cartMethodClass.getCart(session,
                (UserEntity) session.getAttribute("user"));
        return ResponseEntity.ok(cartList != null ? cartList : new HashMap<>());
    }

    @PostMapping("addToCart")
    public ResponseEntity<Void> addToCart(@RequestBody Map<String, Object> params, HttpSession session) {
        choiceUser(session); // リクエスト毎にユーザーを判定
        UserEntity user = (UserEntity) session.getAttribute("user");
        cartMethodClass.addBookToCart(params, user, session);
        return ResponseEntity.noContent().build();
    }
}
