package com.beauty_saloon_backend.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailService {
    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    public void sendContactFormToAdmin(String name, String email, String phone, String message) throws IOException {
        Email from = new Email("kapcsolat@innovweb.hu");
        String subjectText = "Új kapcsolatfelvételi űrlap";
        Email to = new Email("kapcsolat@innovweb.hu");
        Content content = new Content("text/plain",
                "Név: " + name + "\n" +
                        "Email: " + email + "\n" +
                        "Tárgy: " + phone + "\n" +
                        "Üzenet: " + message);

        Mail mail = new Mail(from, subjectText, to, content);
        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sg.api(request);
        } catch (IOException ex) {
            throw ex;
        }
    }

    public void sendConfirmationEmail(String recipientEmail, String name) throws IOException {
        Email from = new Email("kapcsolat@innovweb.hu");
        String subject = "Köszönjük a kapcsolatfelvételt!";
        Email to = new Email(recipientEmail);
        Content content = new Content("text/plain",
                "Kedves " + name + ",\n\n" +
                        "Köszönjük, hogy felvette velünk a kapcsolatot.\n\n" +
                        "Hamarosan válaszolunk Önnek.\n\n" +
                        "Üdvözlettel:\nBeauty Szépségszalon\n");

        Mail mail = new Mail(from, subject, to, content);
        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sg.api(request);
        } catch (IOException ex) {
            throw ex;
        }
    }
}
