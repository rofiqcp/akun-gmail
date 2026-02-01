package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmailService {
    
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    
    @Autowired(required = false)
    private JavaMailSender mailSender;
    
    public void sendVerificationEmail(String toEmail, String verificationToken, String applicationUrl) {
        try {
            if (mailSender == null) {
                logger.warn("Mail sender not configured. Skipping email send.");
                logger.info("Verification link for {}: {}/verify-email?token={}", 
                    toEmail, applicationUrl, verificationToken);
                return;
            }
            
            String verificationLink = applicationUrl + "/verify-email?token=" + verificationToken;
            
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Email Verification - Gmail Automation");
            message.setText("Hello,\n\n" +
                "Please click the link below to verify your email:\n\n" +
                verificationLink + "\n\n" +
                "This link will expire in 24 hours.\n\n" +
                "Best regards,\n" +
                "Gmail Automation Team");
            message.setFrom("noreply@gmailautomation.com");
            
            mailSender.send(message);
            logger.info("Verification email sent to: {}", toEmail);
            
        } catch (Exception e) {
            logger.error("Error sending verification email to {}: {}", toEmail, e.getMessage());
            throw new RuntimeException("Failed to send verification email", e);
        }
    }
    
    public void sendWelcomeEmail(String toEmail, String userName) {
        try {
            if (mailSender == null) {
                logger.info("Welcome email would be sent to: {}", toEmail);
                return;
            }
            
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Welcome to Gmail Automation");
            message.setText("Hello " + userName + ",\n\n" +
                "Welcome to Gmail Automation!\n\n" +
                "Your email has been verified and your account is ready to use.\n\n" +
                "Best regards,\n" +
                "Gmail Automation Team");
            message.setFrom("noreply@gmailautomation.com");
            
            mailSender.send(message);
            logger.info("Welcome email sent to: {}", toEmail);
            
        } catch (Exception e) {
            logger.error("Error sending welcome email to {}: {}", toEmail, e.getMessage());
        }
    }
}
