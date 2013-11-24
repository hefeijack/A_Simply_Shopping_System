/**
 * 该控制器响应用户购买商品的请求
 */

package com.jackge.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jackge.service.BookService;
import com.jackge.service.MyCart;

public class ShoppingClServlet extends HttpServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		
		//接收type，区分用户希望做什么（del / add / udpate ）
		String type = request.getParameter("type");
		if(type.equals("del")){
			//说明用户希望从购物车删除商品
			
			//接收用户想删除商品的id
			String id = request.getParameter("id");
			//得到购物车（从session中去购物车）
			MyCart myCart = (MyCart) request.getSession().getAttribute("myCart");
			myCart.delBook(id);
			//把要显示的商品信息放入request
			request.setAttribute("bookList", myCart.showMyCart());
			//调回showMyCar.jsp
			request.getRequestDispatcher("/WEB-INF/showMyCar.jsp").forward(request, response);
			
		}else if(type.equals("add")){
			//说明用户希望添加商品
			//接收用户想购买的商品的id
			String id = request.getParameter("id");
			System.out.println("购买的书号： " + id);

			//什么时候创建购物车？（当用户登录成功后，为它创建一个购物车）
			//取出购物车并添加书到购物车中
			MyCart myCart = (MyCart) request.getSession().getAttribute("myCart");
			myCart.addBook2(id);
//			myCart.addBook(id, new BookService().getBookById(id));
			
			//把要显示的数据放入request中准备显示
			request.setAttribute("bookList", myCart.showMyCart());
			request.setAttribute("totalPrice", myCart.getTotalPrice()+"");
			
			//跳转到显示我的购物车
			request.getRequestDispatcher("/WEB-INF/showMyCar.jsp").forward(request, response);
		}else if(type.equals("update")){
			//更新
			
//			System.out.println("用户希望更新！");
			//得到用户希望更新的书号和数量
			String bookIds[] = request.getParameterValues("id");
			//得到每本书的数量
			String nums[] = request.getParameterValues("booknum");
			MyCart myCart = (MyCart) request.getSession().getAttribute("myCart");
			for(int i=0; i<bookIds.length; i++){
				//更新整个购物车
				myCart.updateBook(bookIds[i], nums[i]);
			}
			
			//跳转回我的购物车
			//把要显示的数据放入request中准备显示
			request.setAttribute("bookList", myCart.showMyCart());
			request.setAttribute("totalPrice", myCart.getTotalPrice()+"");
			
			//跳转到显示我的购物车
			request.getRequestDispatcher("/WEB-INF/showMyCar.jsp").forward(request, response);
			
		}

		
		
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {


		this.doGet(request, response);
	}

}
