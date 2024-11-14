package com.example.demo.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.UserEntity;
import com.example.demo.service.CartMethodClass;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("bookpurchase")
public class BookPurchaseController {


	@Autowired
	@Qualifier("loginCartService")
	private CartMethodClass loginCartService;

	@Autowired
	@Qualifier("guestCartService")
	private CartMethodClass guestCartService;

	private CartMethodClass cartMethodClass;

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
		
		choiceUser(session);		
		Map<Integer, Map<String, Object>> cartList = cartMethodClass.getCart(session, (UserEntity) session.getAttribute("user"));	
		model.addAttribute("cartList", cartList);

		return "bookpurchase/regist";
	}

	@PostMapping("/removeItem")
	public String removeItem(HttpSession session, Integer bookId, Model model) {
		
		choiceUser(session);
		UserEntity user = (UserEntity) session.getAttribute("user");
		cartMethodClass.removeItem(session, user, bookId);
		
		return "redirect:/bookpurchase/regist";
	}
}
