/**
 * 这个表示我的购物车
 */

package com.jackge.service;

import java.util.*;

import com.jackge.domain.Book;

public class MyCart {
	
	HashMap<String, Book> hm = new HashMap<String, Book>();
	
	//添加书的第二个方法
	public void addBook2(String id){
		if(hm.containsKey(id)){
			Book book = hm.get(id);
			int shoppingNum = book.getShoppingNum();
			book.setShoppingNum(shoppingNum+1);
		}else{
			hm.put(id, new BookService().getBookById(id));
		}
	}
	
	//添加书
	public void addBook(String id,Book book){
		if(hm.containsKey(id)){
			book = hm.get(id);
			//如果已经购买过了
			int shoppingNum = book.getShoppingNum();
			book.setShoppingNum(shoppingNum+1);
//			hm.put(id, book);
		}else{
			hm.put(id, book);
		}
	}
	
	//删除书
	public void delBook(String id){
		hm.remove(id);
	}
	
	//更新书
	public void updateBook(String id, String nums){
		//取出id对应的book
		Book book = hm.get(id);
		book.setShoppingNum(Integer.parseInt(nums));
	}
	
	//显示该购物车中的所有商品信息
	public ArrayList showMyCart(){
		ArrayList<Book> al = new ArrayList<Book>();
		//遍历HashMap
		Iterator iterator = hm.keySet().iterator();
		while(iterator.hasNext()){
			//取出key
			String id = (String)iterator.next();
			//取出Book
			Book book = hm.get(id);
			al.add(book);
		}
		return al;
	}
	
	//返回该购物车的总价
	public float getTotalPrice(){
		
		float totalPrice = 0.0f;
		
		ArrayList<Book> al = new ArrayList<Book>();
		Iterator iterator = hm.keySet().iterator();
		while(iterator.hasNext()){
			//取出书号
			String bookId = (String) iterator.next();
			//取出该书号对应的book
			Book book = hm.get(bookId);
			
			totalPrice += book.getPrice()*book.getShoppingNum();
		}
		return totalPrice;
	}
	
	//清空书，清空购物车
	public void clearBook(){
		hm.clear();
	}
	
	
	
}
