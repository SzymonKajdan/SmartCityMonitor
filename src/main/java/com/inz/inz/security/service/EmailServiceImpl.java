package com.inz.inz.security.service;

import com.inz.inz.exceptionhandler.DbException;
import com.inz.inz.exceptionhandler.ErrorSpecifcation;
import com.inz.inz.exceptionhandler.Field;
import com.inz.inz.repository.UserRepository;
import com.inz.inz.security.model.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    UserRepository userRepository;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername("sharethegamehelpdesk@gmail.com");
        mailSender.setPassword("Qwert1y#");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");


        return mailSender;
    }

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    public void sendPassword(String adress, String subject, String content) throws DbException {


        MimeMessage mail = getJavaMailSender().createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(adress);
            helper.setReplyTo("www.sharethegamehelpdesk@gmail.com");
            helper.setFrom("www.sharethegamehelpdesk@gmail.com");
            helper.setSubject(subject);
            helper.setText(content, true);
        } catch (MessagingException e) {
            throw new DbException(ErrorSpecifcation.SENDINGEMAIL.getDetails(), ErrorSpecifcation.SENDINGEMAIL.getCode(), new Field(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        try {
            getJavaMailSender().send(mail);
        } catch (MailException ex) {
            throw new DbException(ErrorSpecifcation.SENDINGEMAIL.getDetails(), ErrorSpecifcation.SENDINGEMAIL.getCode(), new Field(), HttpStatus.REQUEST_TIMEOUT);
        }
    }

    @Override
    public void resetPassword(String email) throws DbException {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            String password = RandomStringUtils.randomAlphabetic(10);
            user.setPassword(bCryptPasswordEncoder().encode(password));
            userRepository.save(user);
            sendPassword(email, "new password", password);

        } else {
            throw new DbException(ErrorSpecifcation.USERNOTEXIST.getDetails(), ErrorSpecifcation.USERNOTEXIST.getCode(), new Field(), HttpStatus.NOT_FOUND);
        }

    }

}

