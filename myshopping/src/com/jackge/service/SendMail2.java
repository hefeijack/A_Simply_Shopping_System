/**
 * �ó�����sendmail�����ڷ����ʼ���
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

	private MimeMessage mimeMsg;      //MIME�ʼ�����
	
	private Session session;          //�ʼ��Ự����
	private Properties props;         //ϵͳ����
	private boolean needAuth=false;   //smtp�Ƿ���Ҫ��֤
	
	private String username="";       //smtp��֤�û���������
	private String password="";
	
	private Multipart mp;             //Multipart�����ʼ����ݡ����⡢����������
	
	public SendMail2(){
		//setSmtpHost(getConfig.mailHost); //���û��ָ���м������
		createMimeMessage();
	}
	
	public SendMail2(String smtp){
		setSmtpHost(smtp);
		createMimeMessage(); 
	}
	
	public void setSmtpHost(String hostName){
		System.out.println("����ϵͳ���ԣ�mail.stmp.host="+hostName);
		if(props==null) props=System.getProperties();                 //��ȡϵͳ����
		props.put("mail.smtp.host",hostName);                         //����SMTP����
	}
	
	public boolean createMimeMessage(){
		try{
			System.out.println("׼����ȡ�ʼ��Ự����");
			session=Session.getDefaultInstance(props,null);
		}catch(Exception e){
			System.err.println("��ȡ�ʼ��Ự����ʱ��������"+e);
			return false;
		}
		System.out.println("׼������MIME�ʼ�����");
		try{
			mimeMsg =new MimeMessage(session);//����MIME�ʼ�����
			mp=new MimeMultipart();
			
			return true;
		}catch(Exception e){
			System.err.println("����MIME�ʼ�����ʧ��"+e);
			return false;
		}
	}
	
	public void setNeedAnth(boolean need){
		System.out.println("����smtp�����֤��mail.smtp.auth="+need);
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
		System.out.print("�����ʼ�����");
		try{
			mimeMsg.setSubject(mailSubject);
			return true;
		}catch(Exception e){
			System.err.println("�����ʼ����ⷢ������");
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
			System.err.println("�����ʼ������Ƿ�������"+e);
			return false;
		}
	}
	
	public boolean addFileAffix(String filename){
		System.out.println("�����ʼ�������"+filename);
		try{
			
			BodyPart bp=new MimeBodyPart();
			FileDataSource fileds=new FileDataSource(filename);
			bp.setDataHandler(new DataHandler(fileds));
			bp.setFileName(fileds.getName());
			mp.addBodyPart(bp);
			return true;
		}catch(Exception e){
			System.err.println("�����ʼ�����"+filename+"��������"+e);
			return false;
		}
	}
	
	public boolean setForm(String from){
		System.out.println("���÷�����");
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
			
			System.out.println("���ڷ����ʼ�....");
			
			Session mailSession=Session.getInstance(props,null);
			Transport transport=mailSession.getTransport("smtp");
			transport.connect((String)props.get("mail.smtp.host"),username,password);
			transport.sendMessage(mimeMsg, mimeMsg.getRecipients(Message.RecipientType.TO));
			
			System.out.println("�����ʼ��ɹ���");
			transport.close();
			
			return true;
		}catch(Exception e){
			System.out.println("�ʼ�����ʧ�ܣ�"+e);
			return false;
		}
	}
	
//	public static void sendToSomebody(String mailbody,String title,String toEmail){
	public static void main(String[] args){
		//���͵��ʼ�����
		String mailbody="<meta http-equiv=Content-Type content=text/html;charset=gb2312" +
				"<div align=center><h1 color=red>I am jackge �����������ɹ��ˣ�����";
			
		//ָ�����Ǹ�smtpת��
		SendMail2 themail=new SendMail2("smtp.163.com");
		
		//У�����
		themail.setNeedAnth(true);
		
		//�ʼ�����
		if(themail.setSubject("�����ʼ����Ⱑ������")==false) return;
		//��Ҫ���͵����ݷ����ʼ���
		if(themail.setBody(mailbody)==false) return;
		
		//��������
		if(themail.setTo("otherEmail")==false) return;
		//˭���͵�
		if(themail.setForm("yourEmail")==false) return;
		
		//���͸���
		if(themail.addFileAffix("d:/SP001.DBF")==false) return;
		//����163��վ�ϵ��ʼ��û�������������ʼ���
		themail.setNamePass("yourEmail", "youtPWD");
		//����
		if(themail.sendout()==false) return;
	}
}
