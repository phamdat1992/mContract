package vn.amitgroup.digitalsignatureapi.service;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import vn.amitgroup.digitalsignatureapi.dto.Mail;
import vn.amitgroup.digitalsignatureapi.dto.MultipleMail;

@Service
public class EmailSenderService {
    private static final Logger LOGGER = LogManager.getLogger(EmailSenderService.class);
    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    private SpringTemplateEngine templateEngine;
    private ThreadPoolExecutor pool = new ThreadPoolExecutor(2,
            Integer.MAX_VALUE, 10, TimeUnit.MINUTES, new LinkedBlockingQueue<>());
    public void sendEmail(Mail mail) throws MessagingException, IOException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
        Context context = new Context();
        context.setVariables(mail.getProps());
        String html = templateEngine.process(mail.getTemplateName(), context);
        helper.setTo(mail.getMailTo());
        helper.setText(html.replace("linkLogoEmail","https://mcontract.vn/uploads/logo.png"), true);
        helper.setSubject(mail.getSubject());
        helper.setFrom(mail.getFrom(),"MContract Service");
        emailSender.send(message);
    }
    public void sendMulEmail(MultipleMail mails) throws MessagingException, IOException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
        Context context = new Context();
        context.setVariables(mails.getProps());
        String html = templateEngine.process(mails.getTemplateName(), context);
        helper.setTo(mails.getMailTo());
        helper.setText(html, true);
        helper.setSubject(mails.getSubject());
        helper.setFrom(mails.getFrom());
        emailSender.send(message);
    }
    public void sendEmailAsync(Mail mail,boolean async) throws MessagingException, IOException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
        Context context = new Context();
        context.setVariables(mail.getProps());
        String html = templateEngine.process(mail.getTemplateName(), context);
        html = html.replace("linkLogoEmail","https://mcontract.vn/uploads/logo.png");
        helper.setTo(mail.getMailTo());
        helper.setText(html, true);
        helper.setSubject(mail.getSubject());
        helper.setFrom(mail.getFrom(),"MContract Service");
        if (async) {
            this.addSendTask(message);
        } else {
            emailSender.send(message);
        }
    }
    private void addSendTask(final MimeMessage mimeMessage) {
        try {
            pool.execute(() -> {
                emailSender.send(mimeMessage);
                LOGGER.debug("Send successfully !");
            });
        } catch (Exception e) {
            LOGGER.error("Exception when sen email !", e);
        }
    }
}
