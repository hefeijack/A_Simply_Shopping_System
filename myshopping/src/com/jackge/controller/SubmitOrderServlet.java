package com.jackge.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jackge.domain.Users;
import com.jackge.service.MyCart;
import com.jackge.service.OrderService;
import com.jackge.service.SendMail;

public class SubmitOrderServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();

		//��servletҪ�����¶���������
		try{
			
			OrderService orderService = new OrderService();
			
			MyCart myCart = (MyCart) request.getSession().getAttribute("myCart");
			Users user = (Users) request.getSession().getAttribute("loginUser");
			orderService.submitOrder(myCart, user);
			
		}catch(Exception e){
			System.out.println(e.getMessage());
			request.getRequestDispatcher("/WEB-INF/errInfo.jsp");
		}
		
		//�������д�뵽���ݿ⣬���ʼ����͸��ͻ�
		//����һ��SendMail����ʵ��
		SendMail sendMail = new SendMail();
		Users user = (Users) request.getSession().getAttribute("loginUser");
		sendMail.sendToSomebody("<html><body>hihihihihihi</body></html>", user.getName() + "�����������𣿣���", user.getEmail());
		System.out.println("this email is : " + user.getEmail());
		request.getRequestDispatcher("/WEB-INF/orderOk.jsp").forward(request, response);
		
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doGet(request, response);
	}

}
