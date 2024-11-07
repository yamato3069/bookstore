package com.example.demo.service;

import java.util.Map;

import com.example.demo.entity.CartEntity;
import com.example.demo.entity.UserEntity;

import jakarta.servlet.http.HttpSession;

/**
 * カート処理に関するメソッド
 * @LoginCartService ログインユーザー用
 * @GuestCartService ゲストログイン用
 */
public interface CartMethodClass {
	
	//カート情報を取得する処理
	Map<Integer, Map<String, Object>> getCart(HttpSession session, UserEntity user);
	
	//カートに追加する処理
    void addToCart(HttpSession session, UserEntity user, CartEntity cartEntity);
    
    //カートオブジェクトを生成する処理
    CartEntity createCartEntity(Map<String, Object> params, UserEntity user, HttpSession session);

    //カート処理まとめたやつ（オブジェクト生成とinsert）
	void addBookToCart(Map<String, Object> params, UserEntity user, HttpSession session);
	
	// アイテムをカートから削除する処理
    void removeItem(HttpSession session, UserEntity user, Integer bookId);
}
