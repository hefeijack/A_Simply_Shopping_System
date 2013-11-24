/**
 * �ÿ�������Ӧ�û�������Ʒ������
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
		
		//����type�������û�ϣ����ʲô��del / add / udpate ��
		String type = request.getParameter("type");
		if(type.equals("del")){
			//˵���û�ϣ���ӹ��ﳵɾ����Ʒ
			
			//�����û���ɾ����Ʒ��id
			String id = request.getParameter("id");
			//�õ����ﳵ����session��ȥ���ﳵ��
			MyCart myCart = (MyCart) request.getSession().getAttribute("myCart");
			myCart.delBook(id);
			//��Ҫ��ʾ����Ʒ��Ϣ����request
			request.setAttribute("bookList", myCart.showMyCart());
			//����showMyCar.jsp
			request.getRequestDispatcher("/WEB-INF/showMyCar.jsp").forward(request, response);
			
		}else if(type.equals("add")){
			//˵���û�ϣ�������Ʒ
			//�����û��빺�����Ʒ��id
			String id = request.getParameter("id");
			System.out.println("�������ţ� " + id);

			//ʲôʱ�򴴽����ﳵ�������û���¼�ɹ���Ϊ������һ�����ﳵ��
			//ȡ�����ﳵ������鵽���ﳵ��
			MyCart myCart = (MyCart) request.getSession().getAttribute("myCart");
			myCart.addBook2(id);
//			myCart.addBook(id, new BookService().getBookById(id));
			
			//��Ҫ��ʾ�����ݷ���request��׼����ʾ
			request.setAttribute("bookList", myCart.showMyCart());
			request.setAttribute("totalPrice", myCart.getTotalPrice()+"");
			
			//��ת����ʾ�ҵĹ��ﳵ
			request.getRequestDispatcher("/WEB-INF/showMyCar.jsp").forward(request, response);
		}else if(type.equals("update")){
			//����
			
//			System.out.println("�û�ϣ�����£�");
			//�õ��û�ϣ�����µ���ź�����
			String bookIds[] = request.getParameterValues("id");
			//�õ�ÿ���������
			String nums[] = request.getParameterValues("booknum");
			MyCart myCart = (MyCart) request.getSession().getAttribute("myCart");
			for(int i=0; i<bookIds.length; i++){
				//�����������ﳵ
				myCart.updateBook(bookIds[i], nums[i]);
			}
			
			//��ת���ҵĹ��ﳵ
			//��Ҫ��ʾ�����ݷ���request��׼����ʾ
			request.setAttribute("bookList", myCart.showMyCart());
			request.setAttribute("totalPrice", myCart.getTotalPrice()+"");
			
			//��ת����ʾ�ҵĹ��ﳵ
			request.getRequestDispatcher("/WEB-INF/showMyCar.jsp").forward(request, response);
			
		}

		
		
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {


		this.doGet(request, response);
	}

}
