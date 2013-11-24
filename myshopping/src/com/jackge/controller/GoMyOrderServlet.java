/**
 * ��servlet�����û��鿴����������
 */

package com.jackge.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jackge.service.MyCart;

public class GoMyOrderServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();

		//�õ����ﳵ
		MyCart myCart = (MyCart) request.getSession().getAttribute("myCart");
		ArrayList al = myCart.showMyCart();
		float totalPrice = myCart.getTotalPrice();
		request.setAttribute("orderinfo", al);
		request.setAttribute("totalPrice", totalPrice);
		//��ת���ҵĶ�����ҳ��
		request.getRequestDispatcher("/WEB-INF/showMyOrder.jsp").forward(request, response);
		
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doGet(request, response);
	}

}
