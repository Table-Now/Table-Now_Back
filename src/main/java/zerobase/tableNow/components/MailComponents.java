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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@Component
@Slf4j
public class MailComponents {
    private final JavaMailSender javaMailSender;

    public boolean sendMail(String mail, String subject, String text) {
        boolean result = false;
        MimeMessagePreparator msg = mimeMessage -> {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            mimeMessageHelper.setFrom("jominuk1025@gmail.com", "TableNow");
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

    // 예약이 되었는지에 대한 확인 메일
    public String getEmailReservation(String store, LocalDateTime time) {
        return "<div style='margin:50px; padding:20px; font-family:Arial, sans-serif; color:#333; border:1px solid #ddd; border-radius:8px;'>" +
                "<h2 style='color:#007bff;'>예약이 완료되었습니다!</h2>" +
                "<p>아래의 정보로 예약이 완료되었습니다:</p>" +
                "<div style='margin-top:20px;'>" +
                "<p><strong>상점명:</strong> " + store + "</p>" +
                "<p><strong>예약 시간:</strong> " + time.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분")) + "</p>" +
                "</div>" +
                "<br/>" +
                "<p>예약 해주셔서 감사합니다. 감사합니다!</p>" +
                "</div>";
    }

}