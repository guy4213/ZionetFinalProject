package org.example.communicationserviceapp.service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.example.communicationserviceapp.entity.Article;
import org.example.communicationserviceapp.entity.NewsNotification;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CommunicationService {

    private final JavaMailSender mailSender;

    public CommunicationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public MimeMessage sendNewsNotification(NewsNotification newsNotification) {
        // Logic to send email to user with news articles
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(newsNotification.getUser().getEmail());
            helper.setSubject("Your Daily News Update");
            if (buildEmailContent(newsNotification) != null) {
                helper.setText(buildEmailContent(newsNotification), true);
            }
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return message;
    }

    public String buildEmailContent(NewsNotification newsNotification) {
        StringBuilder emailContent = new StringBuilder();

        emailContent.append("<html><body>");
        emailContent.append("<p>Hello ").append(newsNotification.getUser().getUserName()).append(",</p>");
        emailContent.append("<p>Here are the latest news articles based on your preferences:</p>");

        List<Article> articles = newsNotification.getArticles();

        for (int i = 0; i < articles.size(); i++) {
            Article article = articles.get(i);
            //prints category at 1st,4th 7th and 10th indexes - beginnings of each article`s array`s category
            if (i % 3 == 0) {
                emailContent.append("<p><strong>Category: ").append(article.getCategory().get(0)).append("</strong></p>");
            }

            emailContent.append("<p><strong>Title:</strong> ").append(article.getTitle()).append("</p>");
            emailContent.append("<p><strong>URL:</strong> <a href='").append(article.getLink()).append("'>").append(article.getLink()).append("</a></p>");

            // Append category information for the first article


            emailContent.append("<br/>"); // Line break between articles
        }

        emailContent.append("<p>Best regards,<br/>Your News Aggregation Service</p>");
        emailContent.append("</body></html>");

        return emailContent.toString();
    }
}
