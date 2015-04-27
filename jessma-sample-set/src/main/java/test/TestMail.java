package test;

import org.jessma.util.mail.MailSender;

public class TestMail
{
	public static void main(String[] args) throws Exception
	{
		try
		{
			MailSender mail = new MailSender();
			
			mail.setHost("smtp.163.com");			// 设置 SMTP 主机
			mail.setFrom("myname@163.com");			// 设置发送者地址
			mail.addTo("someone@sina.com");			// 添加发送目标地址
			mail.addCc("someelse@sohu.com");			// 添加抄送目标地址
			mail.addFileAcc("C:\\Temp\\90.jpg");	// 添加文件附件
			// 从 byte[] 中读取数据并添加为附件（这个功能有时非常有用）
			mail.addByteAcc("我是大怪兽".getBytes(), MailSender.DEFAULT_CONTENT_TYPE, "我是谁.txt");
			mail.addReplyTo(mail.getFrom());		// 添加回复地址
			mail.setAuth(true);						// 设置验证模式
			mail.setNeedReceipt(true);				// 设置是否需要回执
			mail.setUser("myname");					// 设置邮箱登录名
			mail.setPassword("******");				// 设置邮箱登录密码
			mail.setSubject("hello - 2");			// 设置邮件主题
			mail.setText("test mail");				// 设置邮件文本内容
			mail.setCharset("UTF-8");				// 设置邮件文本内容编码
			mail.setContentType("text/html");		// 设置邮件文本内容格式
			
			mail.send();							// 发送邮件
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
