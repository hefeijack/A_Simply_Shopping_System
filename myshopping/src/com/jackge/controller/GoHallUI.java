package com.jackge.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jackge.domain.Users;
import com.jackge.service.BookService;
import com.jackge.service.MyCart;
import com.jackge.service.UsersService;

public class GoHallUI extends HttpServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		
		//先判断该用户是否已经登录（可读性较差），如果登录了的话，则可以直接跳转到购物大厅
		if(request.getSession().getAttribute("loginUser") != null){
			//给下一个页面hall.jsp准备要显示的数据
			BookService bookService = new BookService();
			ArrayList al = bookService.getAllBook();
			//把显示的数据放入request，因为request对象的生命周期最短
			request.setAttribute("books", al);
			
			request.getRequestDispatcher("/WEB-INF/hall.jsp").forward(request, response);
			return ;
		}

		//得到从登陆页面传递的用户名和密码
		String id = request.getParameter("id");
		String p = request.getParameter("passwd");
		//创建一个User对象
		Users loginUser = new Users(Integer.parseInt(id), p);
		
		//使用业务逻辑类，完成验证
		UsersService usersService = new UsersService();
		if(usersService.checkUser(loginUser)){
			//合法，跳转到购物大厅
			
			//因为在其它的页面都可能使用到用户信息，因此，我们可以把用户信息存放到session
			request.getSession().setAttribute("loginUser", loginUser);
			
			//创建一个购物车
			MyCart myCart = new MyCart();
			request.getSession().setAttribute("myCart", myCart);
			
			//给下一个页面hall.jsp准备要显示的数据
			BookService bookService = new BookService();
			ArrayList al = bookService.getAllBook();
			//把显示的数据放入request，因为request对象的生命周期最短
			request.setAttribute("books", al);
			
			request.getRequestDispatcher("/WEB-INF/hall.jsp").forward(request, response);
		}else{
			//不合法
			request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
		}
		
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doGet(request, response);
	}

}
