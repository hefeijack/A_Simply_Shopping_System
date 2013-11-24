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
		
		//���жϸ��û��Ƿ��Ѿ���¼���ɶ��Խϲ�������¼�˵Ļ��������ֱ����ת���������
		if(request.getSession().getAttribute("loginUser") != null){
			//����һ��ҳ��hall.jsp׼��Ҫ��ʾ������
			BookService bookService = new BookService();
			ArrayList al = bookService.getAllBook();
			//����ʾ�����ݷ���request����Ϊrequest����������������
			request.setAttribute("books", al);
			
			request.getRequestDispatcher("/WEB-INF/hall.jsp").forward(request, response);
			return ;
		}

		//�õ��ӵ�½ҳ�洫�ݵ��û���������
		String id = request.getParameter("id");
		String p = request.getParameter("passwd");
		//����һ��User����
		Users loginUser = new Users(Integer.parseInt(id), p);
		
		//ʹ��ҵ���߼��࣬�����֤
		UsersService usersService = new UsersService();
		if(usersService.checkUser(loginUser)){
			//�Ϸ�����ת���������
			
			//��Ϊ��������ҳ�涼����ʹ�õ��û���Ϣ����ˣ����ǿ��԰��û���Ϣ��ŵ�session
			request.getSession().setAttribute("loginUser", loginUser);
			
			//����һ�����ﳵ
			MyCart myCart = new MyCart();
			request.getSession().setAttribute("myCart", myCart);
			
			//����һ��ҳ��hall.jsp׼��Ҫ��ʾ������
			BookService bookService = new BookService();
			ArrayList al = bookService.getAllBook();
			//����ʾ�����ݷ���request����Ϊrequest����������������
			request.setAttribute("books", al);
			
			request.getRequestDispatcher("/WEB-INF/hall.jsp").forward(request, response);
		}else{
			//���Ϸ�
			request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
		}
		
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doGet(request, response);
	}

}
