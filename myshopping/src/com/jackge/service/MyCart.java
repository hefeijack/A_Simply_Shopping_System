/**
 * �����ʾ�ҵĹ��ﳵ
 */

package com.jackge.service;

import java.util.*;

import com.jackge.domain.Book;

public class MyCart {
	
	HashMap<String, Book> hm = new HashMap<String, Book>();
	
	//�����ĵڶ�������
	public void addBook2(String id){
		if(hm.containsKey(id)){
			Book book = hm.get(id);
			int shoppingNum = book.getShoppingNum();
			book.setShoppingNum(shoppingNum+1);
		}else{
			hm.put(id, new BookService().getBookById(id));
		}
	}
	
	//�����
	public void addBook(String id,Book book){
		if(hm.containsKey(id)){
			book = hm.get(id);
			//����Ѿ��������
			int shoppingNum = book.getShoppingNum();
			book.setShoppingNum(shoppingNum+1);
//			hm.put(id, book);
		}else{
			hm.put(id, book);
		}
	}
	
	//ɾ����
	public void delBook(String id){
		hm.remove(id);
	}
	
	//������
	public void updateBook(String id, String nums){
		//ȡ��id��Ӧ��book
		Book book = hm.get(id);
		book.setShoppingNum(Integer.parseInt(nums));
	}
	
	//��ʾ�ù��ﳵ�е�������Ʒ��Ϣ
	public ArrayList showMyCart(){
		ArrayList<Book> al = new ArrayList<Book>();
		//����HashMap
		Iterator iterator = hm.keySet().iterator();
		while(iterator.hasNext()){
			//ȡ��key
			String id = (String)iterator.next();
			//ȡ��Book
			Book book = hm.get(id);
			al.add(book);
		}
		return al;
	}
	
	//���ظù��ﳵ���ܼ�
	public float getTotalPrice(){
		
		float totalPrice = 0.0f;
		
		ArrayList<Book> al = new ArrayList<Book>();
		Iterator iterator = hm.keySet().iterator();
		while(iterator.hasNext()){
			//ȡ�����
			String bookId = (String) iterator.next();
			//ȡ������Ŷ�Ӧ��book
			Book book = hm.get(bookId);
			
			totalPrice += book.getPrice()*book.getShoppingNum();
		}
		return totalPrice;
	}
	
	//����飬��չ��ﳵ
	public void clearBook(){
		hm.clear();
	}
	
	
	
}
