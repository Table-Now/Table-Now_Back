package zerobase.tableNow.components;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Component
@Slf4j
public class MailComponents {
    private final JavaMailSender javaMailSender;

    public boolean sendMail(String mail, String subject, String text) {
        boolean result = false;
        MimeMessagePreparator msg = mimeMessage -> {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            mimeMessageHelper.setTo(mail);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(text, true);
        };

        try {
            javaMailSender.send(msg);
            result = true;
        } catch (Exception e) {
            log.error("메일 발송 실패: " + e.getMessage(), e);
        }
        return result;
    }

    // 인증 메일 템플릿 생성
    public String getEmailAuthTemplate(String user, String emailAuthKey) {
        String authUrl = "http://localhost:8080/user/email-auth?user=" + user + "&key=" + emailAuthKey;

        return "<div style='margin:100px;'>" +
                "<h1>TableNow 가입을 환영합니다!</h1>" +
                "<br/>" +
                "<p>아래 버튼을 클릭하시면 이메일 인증이 완료됩니다.</p>" +
                "<br/>" +
                "<div style='text-align:center'>" +
                "<a href='" + authUrl + "' style='display:inline-block; padding:10px 20px; " +
                "background-color:#007bff; color:#ffffff; text-decoration:none; border-radius:5px;'>" +
                "이메일 인증하기</a>" +
                "</div>" +
                "</div>";
    }
}