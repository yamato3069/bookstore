package com.example.demo.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dto.BookSearchDto;
import com.example.demo.entity.BookEntity;
import com.example.demo.entity.CartEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.service.BookPurchaseService;
import com.example.demo.service.BookSearchService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("bookSearch")
public class BookSearchController {

	private final BookSearchService bookSearchService;
	private final BookPurchaseService bookPurchaseService;

	public BookSearchController(BookSearchService bookSearchService, BookPurchaseService bookPurchaseService) {
		this.bookSearchService = bookSearchService;
		this.bookPurchaseService = bookPurchaseService;
	}

	@RequestMapping("regist")
	public String regist(HttpSession session, Model model) {
		UserEntity user = (UserEntity) session.getAttribute("user");

		model.addAttribute("user", user);
		return "bookSearch/regist";
	}

	@PostMapping("Search")
	public String Search(BookSearchDto bookSearchDto, HttpSession session, Model model) {

		List<BookEntity> bookList = bookSearchService.searchBooks(bookSearchDto);
		model.addAttribute("bookList", bookList);

		return regist(session, model);
	}

	@GetMapping("getCart")
	public ResponseEntity<Map<Integer, Map<String, Object>>> getCart(HttpSession session) {
		UserEntity user = (UserEntity) session.getAttribute("user");
	    
	    if (user == null) {
	        @SuppressWarnings("unchecked")
	        Map<Integer, Map<String, Object>> cartList = (Map<Integer, Map<String, Object>>) session.getAttribute("cartList");
	        return ResponseEntity.ok(cartList != null ? cartList : new HashMap<>());
	    } else {
	        // ログインユーザーの場合、DBからカート情報を取得
	        List<CartEntity> cartEntities = bookPurchaseService.getCart(user.getUserId());
	        Map<Integer, Map<String, Object>> cartList = new HashMap<>();
	        
	        for (CartEntity cartEntity : cartEntities) {
	            Map<String, Object> item = new HashMap<>();
	            item.put("imagePath", cartEntity.getImagePath());
	            item.put("price", cartEntity.getPrice());
	            item.put("title", cartEntity.getTitle());
	            item.put("authorName", cartEntity.getAuthorName());
	            item.put("quantity", cartEntity.getQuantity());
	            cartList.put(cartEntity.getBookId(), item);
	        }
	        
	        return ResponseEntity.ok(cartList);
	    }
	}

	@PostMapping("addToCart")
	public ResponseEntity<Void> addToCart(@RequestParam("imagePath") String imagePath,
			@RequestParam("bookId") Integer bookId,
			@RequestParam("price") BigDecimal price,
			@RequestParam("title") String title,
			@RequestParam("authorName") String authorName,
			@RequestParam("quantity") Integer quantity,
			HttpSession session) {

		UserEntity user = (UserEntity) session.getAttribute("user");

		if (user == null) {
	        // ゲストの場合
	        @SuppressWarnings("unchecked")
	        Map<Integer, Map<String, Object>> cartList = (Map<Integer, Map<String, Object>>) session.getAttribute("cartList");
	        
	        if (cartList == null) {
	            cartList = new HashMap<>();
	        }

	        Map<String, Object> item = new HashMap<>();
	        item.put("imagePath", imagePath);
	        item.put("price", price);
	        item.put("title", title);
	        item.put("authorName", authorName);
	        item.put("quantity", quantity);
	        cartList.put(bookId, item);
	        session.setAttribute("cartList", cartList);
	    } else {
	        // ログインしている場合
	        CartEntity cartEntity = new CartEntity();
	        cartEntity.setUserId(user.getUserId());
	        cartEntity.setBookId(bookId);
	        cartEntity.setQuantity(quantity);
	        cartEntity.setPrice(price);
	        cartEntity.setTitle(title);
	        cartEntity.setAuthorName(authorName);
	        cartEntity.setImagePath(imagePath);
	        bookPurchaseService.addCart(cartEntity);
	    }

	    return ResponseEntity.noContent().build();
	}
}
