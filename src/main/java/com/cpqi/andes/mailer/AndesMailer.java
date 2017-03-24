/**
 * Copyright 2016 CPQi
 */
package com.cpqi.andes.mailer;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * <p>
 * AndesMailer
 * </p>
 *
 * @author Henrique Guedes <hbezerra@cpqi.com>
 * @since 0.1
 * @version 0.1
 */
public class AndesMailer implements Runnable {
    
    private final String[]     to;
    private final String       subject;
    private final String       msg;
    private final String       mimeType;
    
    public static final String ANDES_EMAIL = "noreply@cpqi.com";
    
    public AndesMailer(String[] to, String subject, String msg, String mimeType) {
	this.to = to;
	this.subject = subject;
	this.msg = msg;
	this.mimeType = mimeType;
    }
    
    @Override
    public void run() {
	Message message = new MimeMessage(getSession());
	
	try {
	    for (String m : to) {
		message.addRecipient(RecipientType.TO, new InternetAddress(m));
	    }
	    
	    message.addFrom(new InternetAddress[] { new InternetAddress(ANDES_EMAIL) });
	    message.setSubject(subject);
	    message.setContent(msg, mimeType);
	    
	    Transport.send(message);
	}
	catch (MessagingException e) {
	    e.printStackTrace();
	}
    }
    
    private Session getSession() {
	Authenticator authenticator = new Authenticator();
	
	Properties properties = new Properties();
	properties.setProperty("mail.smtp.submitter", authenticator.getPasswordAuthentication().getUserName());
	properties.setProperty("mail.smtp.auth", "true");
	
	properties.setProperty("mail.smtp.host", "smtp.cpqi.com");
	properties.setProperty("mail.smtp.port", "25");
	
	return Session.getInstance(properties, authenticator);
    }
    
    private class Authenticator extends javax.mail.Authenticator {
	private final PasswordAuthentication authentication;
	
	public Authenticator() {
	    String username = "hermes@cpqi.com";
	    String password = System.getenv("ADS_MAIL_PW");
	    authentication = new PasswordAuthentication(username, password);
	}
	
	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
	    return authentication;
	}
    }
}
