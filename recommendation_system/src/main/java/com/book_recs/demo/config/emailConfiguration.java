package main.java.com.book_recs.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

// Configuration class for email service setup using Gmail SMTP
@Configuration
public class EmailConfiguration {
    @Value("${spring.mail.username}")
    private String emailUsername;

    @Value("${spring.mail.password}")
    private String password;

    @Bean
    public JavaMailSender javaMailSender() {
        //  mail sender instance
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        
        // Configure SMTP server settings
        mailSender.setHost("smtp.gmail.com");    
        mailSender.setPort(587);                 
        mailSender.setUsername(emailUsername);   
        mailSender.setPassword(password);        

        // configure additional mail properties
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");           
        props.put("mail.smtp.auth", "true");                   
        props.put("mail.smtp.starttls.enable", "true");        
        props.put("mail.debug", "true");                       

        return mailSender;
    }
}
