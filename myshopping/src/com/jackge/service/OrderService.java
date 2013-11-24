/**
 * �����붩����ص�ҵ���߼�
 */

package com.jackge.service;

import com.jackge.domain.Book;
import com.jackge.domain.Users;
import com.jackge.utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;

public class OrderService  {

	private Connection ct = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;
	
	//�¶����漰�����ű��������ű��й�ϵ
	public void submitOrder(MyCart myCart, Users user){
		
		String sql = "insert into orders values(orders_seq.nextval,?,?,sysdate)";
		//��Ϊ��Ӷ���ҵ���߼����ӣ��������ֲ������ر��������ǾͲ�ʹ��SqlHelper������ר������¶���д�����ݿ�Ĳ���
		try{
			
			ct = DBUtil.getConnection();
			//Ϊ�˱�֤���ǵĶ��������ȶ��ģ����Խ���������뼶���������ɴ��У�
			ct.setAutoCommit(false);
			ct.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			
			
			ps = ct.prepareStatement(sql);
			ps.setInt(1, user.getId());
			ps.setFloat(2, myCart.getTotalPrice());
			ps.executeUpdate();
			
			//��εõ��ող���Ķ����Ķ�����
			sql = "select orders_seq.currval from orders";
			ps = ct.prepareStatement(sql);
			rs = ps.executeQuery();
			int orderId = 0;
			if(rs.next()){
				//ȡ���ո����ɵĶ�����
				orderId = rs.getInt(1);
			}
			//�Ѷ���ϸ�ڱ����ɣ������ύ��
			ArrayList al = myCart.showMyCart();
			for(int i=0; i<al.size(); i++){
				Book book = (Book)al.get(i);
				sql = "insert into ordersitem values(ordersitem_seq.nextval,?,?,?)";
				ps = ct.prepareStatement(sql);
				ps.setInt(1, orderId);
				ps.setInt(2, book.getId());
				ps.setInt(3, book.getShoppingNum());
				ps.executeUpdate();
			}
			
			//�����ύ
			ct.commit();
			
		}catch(Exception e){
			try {
				ct.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
//			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}finally{
			DBUtil.close(rs, ps, ct);
		}
		
	}
	
}
