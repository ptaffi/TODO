package util;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class MailService {

    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final int SMTP_PORT = 587;

    private static final String FROM_EMAIL = "";
    private static final String APP_PASSWORD = ""; 

    public static void sendOtp(String toEmail, String otp) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", MailConfig.SMTP_HOST);
        props.put("mail.smtp.port", String.valueOf(MailConfig.SMTP_PORT));

        Session session = Session.getInstance(props, new Authenticator() {
            @Override protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(MailConfig.USERNAME, MailConfig.APP_PASSWORD);
            }
        });

        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(MailConfig.USERNAME));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        msg.setSubject("FINI - OTP Reset Password");

        String body = """
                FINI - Reset Password OTP
                OTP của bạn là: %s
                OTP sẽ hết hạn sau 10 phút.
                Nếu bạn không yêu cầu, hãy bỏ qua email này.
                """.formatted(otp);

        msg.setText(body);
        Transport.send(msg);
    }
}
