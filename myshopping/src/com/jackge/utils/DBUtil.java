package com.jackge.utils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
public class DBUtil {
	//定义变量
	private static Connection ct=null;
	//大多数情况下用preparedstatement替代atatement
	private static PreparedStatement ps=null;
	private static ResultSet rs=null;
	//连接数据库的参数
	private  static String url="";
	private static String username="";
	private static String driver="";
	private static String password="";
	//读配置文件
	private static Properties pp=null;
	private static InputStream fis=null;
	
	//加载驱动，只需要一次，用静态代码块
	static{
		try{
			//从dbinfo.properties文件中读取配置信息
			pp=new Properties();
			//fis=new FileInputStream("dbinfo.properties");//=>tomcat的主目录？
			//当我们使用java web的时候，读取文件要使用类加载器[因为类加载器去读取资源的时候，默认的主目录是src目录
			fis=DBUtil.class.getClassLoader().getResourceAsStream("dbinfo.properties");
			
			pp.load(fis);
			url=pp.getProperty("url");
			username=pp.getProperty("username");
			driver=pp.getProperty("driver");
			password=pp.getProperty("password");
			//1.加载驱动
			Class.forName(driver);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				fis.close();
			}catch(Exception e){
				e.printStackTrace();
			}
			fis=null;    //垃圾回收站上收拾
		}
	}
	//得到连接
	public static Connection getConnection(){
		try{
			//建立连接
			ct=DriverManager.getConnection(url,username,password);
		}catch(Exception e){
			e.printStackTrace();
		}
		return ct;
	}
	//关闭资源的函数
	public static void close(ResultSet rs,Statement ps,Connection ct){
		if(rs!=null){
			try{
				rs.close();
			}catch(Exception e){
				e.printStackTrace();
			}
			rs=null;
		}
		if(ps!=null){
			try{
				ps.close();
			}catch(Exception e){
				e.printStackTrace();
			}
			ps=null;
		}
		if(ct!=null){
			try{
				ct.close();
			}catch(Exception e){
				e.printStackTrace();
			}
			ct=null;
		}
	}
}
