package ml.kalanblowSystemManagement.service;

import javax.mail.MessagingException;

import ml.kalanblowSystemManagement.email.AbstractEmailContext;
public interface KalanblowEmailService {
	

    void sendMail(final AbstractEmailContext email) throws MessagingException;



}
