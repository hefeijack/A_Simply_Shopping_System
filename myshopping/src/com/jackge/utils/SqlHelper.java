package com.jackge.utils;

import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

public class SqlHelper {

	//定义需要的变量
	private static Connection ct=null;
	//大多数情况下，我们使用的是PreparedStatement来替代Statement，可以防止sql注入
	private static PreparedStatement ps=null;
	private static ResultSet rs=null;
	private static CallableStatement cs=null;

	//调用存储过程，又返回Result
	//sql call过程（？？？）
	public static CallableStatement callPro2(String sql,String[] inparameters,Integer[] outparameters){
		try{
			ct=DBUtil.getConnection();
			cs=ct.prepareCall(sql);
			if(inparameters!=null){
				for(int i=0;i<inparameters.length;i++){
					cs.setObject(i+1, inparameters[i]);
				}
			}
		
		//给out参数赋值
		if(outparameters!=null){
			for(int i=0;i<outparameters.length;i++){
				cs.registerOutParameter(inparameters.length+1+i,outparameters[i] );
			}
		}
		cs.execute();
	
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
		}
		return cs;
	}
	//调用存储过程
	//sql象{sql过程（？？？）}
	public static void callProl(String sql,String[] parameters){
		try{
			ct=DBUtil.getConnection();
			cs=ct.prepareCall(sql);
			if(parameters!=null){
				for(int i=0;i<parameters.length;i++){
					cs.setObject(i+1, parameters[i]);
				}
			}
			cs.execute();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			DBUtil.close(rs,cs,ct);
		}
	}
	
	//把查询封装成一个函数
	//sql=select  ?,?,?from ? where ?=?.
	public ArrayList executeQuery(String sql,String []parameters){
		ArrayList al=new ArrayList();
		try{
			ct=DBUtil.getConnection();
			ps=ct.prepareStatement(sql);
			//给sql的问号赋值
			for(int i=0;i<parameters.length;i++){
				ps.setString(i+1, parameters[i]);
			}
			rs=ps.executeQuery();
			//这句话非常有用
			ResultSetMetaData rsmd=rs.getMetaData();
			//通过rsmd可以得到该结果集有多少列
			int columnNum=rsmd.getColumnCount();
			//循环的从al中取出数据，并封装到ArrayList中
			while(rs.next()){
				Object []objects=new Object[columnNum];
				for( int i=0;i<objects.length;i++){
					objects[i]=rs.getObject(i+1);
				}
				al.add(objects);
			}
			return al;
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}finally{
			DBUtil.close(rs, ps, ct);
		}
	}
	//如果有多个update/delete/insert[需要考虑事务]
	public static void executeUpdate2(String sql[],String[][] parameters){
		try{
			//核心
			//1.获得连接
			ct=DBUtil.getConnection();
			//因为这时，用户传入的可能是多个Sql语句
			ct.setAutoCommit(false);
			for(int i=0;i<sql.length;i++){
				if(parameters[i]!=null){
					ps=ct.prepareStatement(sql[i]);
					for(int j=0;j<parameters[i].length;i++){
						ps.setString(j+1, parameters[i][j]);
					}
					ps.executeUpdate();
				}
			}
			ct.commit();
		}catch(Exception e){
			e.printStackTrace();
			//回滚
			try{
				ct.rollback();
			}catch(Exception e1){
				e1.printStackTrace();
			}
			throw new RuntimeException(e.getMessage());
		}
		finally{
			DBUtil.close(rs,ps,ct);
		}
	}
	//先写一个update/delete/insert
	//sql格式： update 表明 set 字段名=？ where 字段=？
	//parameters应该是{“abc",23};
	public boolean executeUpdate(String sql,String[] parameters){
		boolean b=false;
		//1.创建一个ps
		try{
			ct=DBUtil.getConnection();
			ps=ct.prepareStatement(sql);
			//给？赋值
			if(parameters!=null){
				for(int i=0;i<parameters.length;i++){
					ps.setString(i+1, parameters[i]);
				}
			}
			//执行
			int num=ps.executeUpdate();
			if(num==1){
				b=true;
			}

		}catch(Exception e){
			e.printStackTrace();//开发阶段
			//抛出异常，抛出运行异常，可以给调用该函数的函数一个选择
			//可以处理，也可以放弃处理
			throw new RuntimeException(e.getMessage());
		}finally{
			//关闭资源
			DBUtil.close(rs,ps,ct);
		}
		return b;
	}
}
