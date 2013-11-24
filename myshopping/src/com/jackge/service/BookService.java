/**
 * 这是一个业务逻辑类，用于处理与book表相关的业务
 */

package com.jackge.service;

import java.util.ArrayList;

import com.jackge.domain.Book;
import com.jackge.utils.SqlHelper;

public class BookService {

	//根据书的编号，返回一个Book
	public Book getBookById(String id){
		Book book = new Book();
		String sql = "select * from book where id=?";
		String paras[] = {id};
		ArrayList al = new SqlHelper().executeQuery(sql, paras);
		if(al.size() == 1){
			Object[] obj = (Object[])al.get(0);
			book.setId(Integer.parseInt(obj[0].toString()));
			book.setName(obj[1].toString());
			book.setAuthor(obj[2].toString());
			book.setPublishHouse(obj[3].toString());
			book.setPrice(Float.parseFloat(obj[4].toString()));
			book.setNums(Integer.parseInt(obj[5].toString()));
		}
		return book;
	}
	
	//得到所有的书籍（分页）
	public ArrayList getAllBook(){
		
		String sql = "select * from book where 1=?";
		String paras[] = {"1"};
		
		ArrayList al = new SqlHelper().executeQuery(sql, paras);
		ArrayList<Book> newAl = new ArrayList<Book>();
		//二次业务封装
		for(int i=0; i<al.size(); i++){
			Object obj[] = (Object[])al.get(i);
			Book book = new Book();
			book.setId(Integer.parseInt(obj[0].toString()));
			book.setName(obj[1].toString());
			book.setAuthor(obj[2].toString());
			book.setPublishHouse(obj[3].toString());
			book.setPrice(Float.parseFloat(obj[4].toString()));
			book.setNums(Integer.parseInt(obj[5].toString()));
			newAl.add(book);
		}
		
		return newAl;
	}
	
}
