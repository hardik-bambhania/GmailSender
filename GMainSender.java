package com.hardik.payment.bl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Security;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class GMainSender extends javax.mail.Authenticator {

    private static final String TAG = "Hardik-GMainSender";

    private static final String PASSWORD = "abc12345";
    private static final String USER = "abc@gmail.com";

    private static GMainSender mInstance;

    private GMainSender() {
        super();
    }

    public static GMainSender getInstance() {
        if (mInstance == null) {
            mInstance = new GMainSender();
        }

        return mInstance;
    }

    static {
        Security.addProvider(new JSSEProvider());
    }

    private Session createSession() {

        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.host", "smtp.gmail.com");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.debug", "true");

        Session session = Session.getInstance(properties, mInstance);

        return session;
    }


    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(USER, PASSWORD);
    }

    private Message createMessage(Session session, String to, String subject, String body) {

        try {
            MimeMessage message = new MimeMessage(session);
            /*DataHandler handler = new DataHandler(new ByteArrayDataSource(body.getBytes(), "text/plain"));
            message.setDataHandler(handler);*/
            message.setContent(body,"text/plain");
            message.setSender(new InternetAddress(USER));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            return message;
        } catch (Exception e) {
            Logger.e(TAG, "Error in create Gmail Message : " + e.getMessage(), e);
        }
        return null;
    }

    public synchronized void sendMail(String subject, String body, String to) {

        try {
            Session session = createSession();
            Message message = createMessage(session, to, subject, body);
            Transport.send(message);

        } catch (Exception e) {
            Logger.e(TAG, "Error in send Gmail : " + e.getMessage(), e);
        }

    }


    public class ByteArrayDataSource implements DataSource {
        private byte[] data;
        private String type;

        public ByteArrayDataSource(byte[] data, String type) {
            super();
            this.data = data;
            this.type = type;
        }

        public ByteArrayDataSource(byte[] data) {
            super();
            this.data = data;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContentType() {
            if (type == null)
                return "application/octet-stream";
            else
                return type;
        }

        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(data);
        }

        public String getName() {
            return "ByteArrayDataSource";
        }

        public OutputStream getOutputStream() throws IOException {
            throw new IOException("Not Supported");
        }
    }

}

