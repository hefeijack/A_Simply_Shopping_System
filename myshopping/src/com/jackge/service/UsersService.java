/**
 * 这是专门用于处理业务逻辑的类（处理和users表相关的业务逻辑）
 */

package com.jackge.service;

import java.util.ArrayList;

import com.jackge.domain.Users;
import com.jackge.utils.SqlHelper;

public class UsersService {

	//验证用户是否合法的方法
	public boolean checkUser(Users user){
		
		//到数据库去验证
		String sql = "select * from users where id=? and pwd=?";
		String paras[] = {user.getId()+"", user.getPwd()};
		ArrayList al = new SqlHelper().executeQuery(sql, paras);
		
		if(al.size() == 0){
			return false;
		}
		
		Object[] objects = (Object[])al.get(0);
		//把对象数组封装到Users对象中
		user.setName((String)objects[1]);
		user.setEmail((String)objects[3]);
		user.setGrade(Integer.parseInt(objects[5].toString()));
		return true;
	}
	
}
