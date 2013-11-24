/**
 * ����ר�����ڴ���ҵ���߼����ࣨ�����users����ص�ҵ���߼���
 */

package com.jackge.service;

import java.util.ArrayList;

import com.jackge.domain.Users;
import com.jackge.utils.SqlHelper;

public class UsersService {

	//��֤�û��Ƿ�Ϸ��ķ���
	public boolean checkUser(Users user){
		
		//�����ݿ�ȥ��֤
		String sql = "select * from users where id=? and pwd=?";
		String paras[] = {user.getId()+"", user.getPwd()};
		ArrayList al = new SqlHelper().executeQuery(sql, paras);
		
		if(al.size() == 0){
			return false;
		}
		
		Object[] objects = (Object[])al.get(0);
		//�Ѷ��������װ��Users������
		user.setName((String)objects[1]);
		user.setEmail((String)objects[3]);
		user.setGrade(Integer.parseInt(objects[5].toString()));
		return true;
	}
	
}
