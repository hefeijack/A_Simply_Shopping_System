/**
 * 该程序是sendmail，用于发送邮件的
 */

package com.jackge.service;

import java.util.*;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendMail2 {

	private MimeMessage mimeMsg;      //MIME邮件对象
	
	private Session session;          //邮件会话对象
	private Properties props;         //系统属性
	private boolean needAuth=false;   //smtp是否需要认证
	
	private String username="";       //smtp认证用户名和密码
	private String password="";
	
	private Multipart mp;             //Multipart对象，邮件内容、标题、附件等内容
	
	public SendMail2(){
		//setSmtpHost(getConfig.mailHost); //如果没有指定有加你服务
		createMimeMessage();
	}
	
	public SendMail2(String smtp){
		setSmtpHost(smtp);
		createMimeMessage(); 
	}
	
	public void setSmtpHost(String hostName){
		System.out.println("设置系统属性：mail.stmp.host="+hostName);
		if(props==null) props=System.getProperties();                 //获取系统属性
		props.put("mail.smtp.host",hostName);                         //设置SMTP主机
	}
	
	public boolean createMimeMessage(){
		try{
			System.out.println("准备获取邮件会话对象！");
			session=Session.getDefaultInstance(props,null);
		}catch(Exception e){
			System.err.println("获取邮件会话对象时发生错误！"+e);
			return false;
		}
		System.out.println("准备创建MIME邮件对象");
		try{
			mimeMsg =new MimeMessage(session);//创建MIME邮件对象
			mp=new MimeMultipart();
			
			return true;
		}catch(Exception e){
			System.err.println("创建MIME邮件对象失败"+e);
			return false;
		}
	}
	
	public void setNeedAnth(boolean need){
		System.out.println("设置smtp身份认证：mail.smtp.auth="+need);
		if(props==null) props=System.getProperties();
		
		if(need){
			props.put("mail.smtp.auth", "true");
		}else{
			props.put("mail.smtp.auth", false);
		}
	}
	
	public void setNamePass(String name,String pass){
		username=name;
		password=pass;
	}
	
	public boolean setSubject(String mailSubject){
		System.out.print("设置邮件主题");
		try{
			mimeMsg.setSubject(mailSubject);
			return true;
		}catch(Exception e){
			System.err.println("设置邮件主题发生错误");
			return false;
		}
	}
	
	public boolean setBody(String mailBody){
		try{
			BodyPart bp=new MimeBodyPart();
			bp.setContent("<meta http-equiv=Content-Type content=text/html; charset=gb2312/>"+mailBody,"text/html;charset=gb2312");
			mp.addBodyPart(bp);
			
			return true;
		}catch(Exception e){
			System.err.println("设置邮件正文是发生错误！"+e);
			return false;
		}
	}
	
	public boolean addFileAffix(String filename){
		System.out.println("增加邮件附件："+filename);
		try{
			
			BodyPart bp=new MimeBodyPart();
			FileDataSource fileds=new FileDataSource(filename);
			bp.setDataHandler(new DataHandler(fileds));
			bp.setFileName(fileds.getName());
			mp.addBodyPart(bp);
			return true;
		}catch(Exception e){
			System.err.println("增加邮件附件"+filename+"发生错误："+e);
			return false;
		}
	}
	
	public boolean setForm(String from){
		System.out.println("设置发信人");
		try{
			mimeMsg.setFrom(new InternetAddress(from));
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	public boolean setTo(String to){
		if(to==null) return false;
		try{
			mimeMsg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	public boolean setCopyTp(String copyto){
		if(copyto==null) return false;
		try{
			mimeMsg.setRecipients(Message.RecipientType.CC, (Address[])InternetAddress.parse(copyto));
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	public boolean sendout(){
		try{
			mimeMsg.setContent(mp);
			//mimeMsg.saveChanges();
			
			System.out.println("正在发送邮件....");
			
			Session mailSession=Session.getInstance(props,null);
			Transport transport=mailSession.getTransport("smtp");
			transport.connect((String)props.get("mail.smtp.host"),username,password);
			transport.sendMessage(mimeMsg, mimeMsg.getRecipients(Message.RecipientType.TO));
			
			System.out.println("发送邮件成功！");
			transport.close();
			
			return true;
		}catch(Exception e){
			System.out.println("邮件发送失败！"+e);
			return false;
		}
	}
	
//	public static void sendToSomebody(String mailbody,String title,String toEmail){
	public static void main(String[] args){
		//发送的邮件内容
		String mailbody="<meta http-equiv=Content-Type content=text/html;charset=gb2312" +
				"<div align=center><h1 color=red>I am jackge ，哈哈哈，成功了！！！";
			
		//指明让那个smtp转发
		SendMail2 themail=new SendMail2("smtp.163.com");
		
		//校验身份
		themail.setNeedAnth(true);
		
		//邮件标题
		if(themail.setSubject("这是邮件标题啊！！！")==false) return;
		//将要发送的内容放入邮件体
		if(themail.setBody(mailbody)==false) return;
		
		//发往哪里
		if(themail.setTo("otherEmail")==false) return;
		//谁发送的
		if(themail.setForm("yourEmail")==false) return;
		
		//发送附件
		if(themail.addFileAffix("d:/SP001.DBF")==false) return;
		//将在163网站上的邮件用户名和密码放入邮件体
		themail.setNamePass("yourEmail", "youtPWD");
		//发送
		if(themail.sendout()==false) return;
	}
}
