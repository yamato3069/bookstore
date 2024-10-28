package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.CartEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.service.BookPurchaseService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("bookpurchase")
public class BookPurchaseController {
	
	private final BookPurchaseService bookPurchaseService;
	
	public BookPurchaseController(BookPurchaseService bookPurchaseService) {
		this.bookPurchaseService = bookPurchaseService;
	}
	
	@RequestMapping("regist")
	public String regist(HttpSession session, Model model) {
		UserEntity user = (UserEntity) session.getAttribute("user");
		model.addAttribute("user", user);
		
	    if (user != null) {
	        List<CartEntity> cartList = bookPurchaseService.getCart(user.getUserId());
	        model.addAttribute("cartList", cartList);
	    } else {
	        model.addAttribute("cartList", session.getAttribute("cartList"));
	    }
	    return "bookpurchase/regist";
	}
	
	@PostMapping("/removeItem")
	public String removeItem(HttpSession session, Integer bookId, Model model) {
	    UserEntity user = (UserEntity) session.getAttribute("user");

	    if (user != null) {
	        // DBのカート情報を1つ減らす
	    	CartEntity cart = bookPurchaseService.getCartByBookId(user.getUserId(), bookId);
	        bookPurchaseService.removeItemFromDb(cart, user.getUserId(), bookId);
	    } else {
	        // セッションのカート情報を1つ減らす
	        @SuppressWarnings("unchecked")
	        Map<Integer, Map<String, Object>> cartList = (Map<Integer, Map<String, Object>>) session.getAttribute("cartList");
	        bookPurchaseService.removeItemFromSession(cartList, bookId);
	        session.setAttribute("cartList", cartList);
	    }
	    
	    return "redirect:/bookpurchase/regist";
	}
	
	@PostMapping("purchase")
	public String purchase(HttpSession session, Model model) {
		//ordersテーブルとorder_itemテーブルにインサート
		
		//購入された分だけbookテーブルからquantity減らす
		return "orderComplete";
	}
}
