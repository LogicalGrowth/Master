package cr.ac.ucenfotec.fun4fund.service;

import cr.ac.ucenfotec.fun4fund.domain.User;

import io.github.jhipster.config.JHipsterProperties;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

/**
 * Service for sending emails.
 * <p>
 * We use the {@link Async} annotation to send emails asynchronously.
 */
@Service
public class MailService {

    private final Logger log = LoggerFactory.getLogger(MailService.class);

    private static final String USER = "user";

    private static final String BASE_URL = "baseUrl";

    private final JHipsterProperties jHipsterProperties;

    private final JavaMailSender javaMailSender;

    private final MessageSource messageSource;

    private final SpringTemplateEngine templateEngine;

    final String template = "<body class='respond' leftmargin='0' topmargin='0' marginwidth='0' marginheight='0'>" +
        "    <table border='0' width='100%' cellpadding='0' cellspacing='0' bgcolor='ffffff'>" +

        "        <tbody><tr>" +
        "            <td align='center'>" +
        "                <table border='0' align='center' width='590' cellpadding='0' cellspacing='0' class='container590'>" +

        "                    <tbody><tr>" +
        "                        <td height='25' style='font-size: 25px; line-height: 25px;'>&nbsp;</td>" +
        "                    </tr>" +

        "                    <tr>" +
        "                        <td align='center'>" +

        "                            <table border='0' align='center' width='590' cellpadding='0' cellspacing='0' class='container590'>" +

        "                                <tbody><tr>" +
        "                                    <td align='center' height='70' style='height:70px;'>" +
        "                                        <a href='' style='display: block; border-style: none !important; border: 0 !important;'><img width='100' border='0' style='display: block; width: 100px;' src='https://prod-fun-4-fund.herokuapp.com/content/c75749885772b68b8c90b48494f53993.png' alt=''></a>" +
        "                                    </td>" +
        "                                </tr>" +
        "                            </tbody></table>" +
        "                        </td>" +
        "                    </tr>" +

        "                    <tr>" +
        "                        <td height='25' style='font-size: 25px; line-height: 25px;'>&nbsp;</td>" +
        "                    </tr>" +

        "                </tbody></table>" +
        "            </td>" +
        "        </tr>" +
        "    </tbody></table>" +
        "    <!-- end header -->" +

        "    <!-- big image section -->" +
        "    <table border='0' width='100%' cellpadding='0' cellspacing='0' bgcolor='ffffff' class='bg_color'>" +

        "        <tbody><tr>" +
        "            <td align='center'>" +
        "                <table border='0' align='center' width='590' cellpadding='0' cellspacing='0' class='container590'>" +
        "                    <tbody><tr>" +

        "                        <td align='center' class='section-img'>" +
        "                            <a href='' style=' border-style: none !important; display: block; border: 0 !important;'><img src='https://res.cloudinary.com/dbk8m5dcv/image/upload/v1607736730/fun4found/toa-heftiba-fbCxL_wEo5M-unsplash_leepc0.jpg' style='display: block; width: 590px;' width='590' border='0' alt=''></a>" +




        "                        </td>" +
        "                    </tr>" +
        "                    <tr>" +
        "                        <td height='20' style='font-size: 20px; line-height: 20px;'>&nbsp;</td>" +
        "                    </tr>" +
        "                    <tr>" +
        "                        <td align='center' style='color: #343434; font-size: 24px; font-family: Quicksand, Calibri, sans-serif; font-weight:700;letter-spacing: 3px; line-height: 35px;' class='main-header'>" +


        "                            <div style='line-height: 35px'>" +

        "                                {{CONTENT}}" +

        "                            </div>" +
        "                        </td>" +
        "                    </tr>" +

        "                    <tr>" +
        "                        <td height='10' style='font-size: 10px; line-height: 10px;'>&nbsp;</td>" +
        "                    </tr>" +

        "                    <tr>" +
        "                        <td align='center'>" +
        "                            <table border='0' width='40' align='center' cellpadding='0' cellspacing='0' bgcolor='eeeeee'>" +
        "                                <tbody><tr>" +
        "                                    <td height='2' style='font-size: 2px; line-height: 2px;'>&nbsp;</td>" +
        "                                </tr>" +
        "                            </tbody></table>" +
        "                        </td>" +
        "                    </tr>" +

        "                    <tr>" +
        "                        <td height='20' style='font-size: 20px; line-height: 20px;'>&nbsp;</td>" +
        "                    </tr>" +

        "                    <tr>" +
        "                        <td align='center'>" +
        "                            <table border='0' width='400' align='center' cellpadding='0' cellspacing='0' class='container590'>" +
        "                                <tbody><tr>" +
        "                                    <td align='center' style='color: #888888; font-size: 16px; font-family: 'Work Sans', Calibri, sans-serif; line-height: 24px;'>" +


        "                                        <div style='line-height: 24px'>" +

        "                                            Gracias por apoyar nuestra plataforma." +
        "                                        </div>" +
        "                                    </td>" +
        "                                </tr>" +
        "                            </tbody></table>" +
        "                        </td>" +
        "                    </tr>" +

        "                    <tr>" +
        "                        <td height='25' style='font-size: 25px; line-height: 25px;'>&nbsp;</td>" +
        "                    </tr>" +

        "                    <tr>" +
        "                        <td align='center'>" +
        "                            <table border='0' align='center' width='160' cellpadding='0' cellspacing='0' bgcolor='5caad2' style=''>" +

        "                                <tbody><tr>" +
        "                                    <td height='10' style='font-size: 10px; line-height: 10px;'>&nbsp;</td>" +
        "                                </tr>" +

        "                                <tr>" +
        "                                    <td align='center' style='color: #ffffff; font-size: 14px; font-family: 'Work Sans', Calibri, sans-serif; line-height: 26px;'>" +


        "                                        <div style='line-height: 26px;'>" +
        "                                            <a style='color: #ffffff; text-decoration: none;' href='https://prod-fun-4-fund.herokuapp.com/'>Visítanos</a>" +
        "                                        </div>" +
        "                                    </td>" +
        "                                </tr>" +

        "                                <tr>" +
        "                                    <td height='10' style='font-size: 10px; line-height: 10px;'>&nbsp;</td>" +
        "                                </tr>" +

        "                            </tbody></table>" +
        "                        </td>" +
        "                    </tr>" +


        "                </tbody></table>" +

        "            </td>" +
        "        </tr>" +

        "    </tbody></table>" +
        "    <!-- end section -->" +

        "    <!-- contact section -->" +
        "    <table border='0' width='100%' cellpadding='0' cellspacing='0' bgcolor='ffffff' class='bg_color'>" +

        "        <tbody><tr class='hide'>" +
        "            <td height='25' style='font-size: 25px; line-height: 25px;'>&nbsp;</td>" +
        "        </tr>" +
        "        <tr>" +
        "            <td height='40' style='font-size: 40px; line-height: 40px;'>&nbsp;</td>" +
        "        </tr>" +

        "        <tr>" +
        "            <td height='60' style='border-top: 1px solid #e0e0e0;font-size: 60px; line-height: 60px;'>&nbsp;</td>" +
        "        </tr>" +

        "        <tr>" +
        "            <td align='center'>" +
        "                <table border='0' align='center' width='590' cellpadding='0' cellspacing='0' class='container590 bg_color'>" +

        "                    <tbody><tr>" +
        "                        <td>" +
        "                            <table border='0' width='300' align='left' cellpadding='0' cellspacing='0' style='border-collapse:collapse; mso-table-lspace:0pt; mso-table-rspace:0pt;' class='container590'>" +

        "                                <tbody><tr>" +
        "                                    <!-- logo -->" +
        "                                    <td align='left'>" +
        "                                        <a href='' style='display: block; border-style: none !important; border: 0 !important;'><img width='80' border='0' style='display: block; width: 80px;' src='https://prod-fun-4-fund.herokuapp.com/content/c75749885772b68b8c90b48494f53993.png' alt=''></a>" +
        "                                    </td>" +
        "                                </tr>" +

        "                                <tr>" +
        "                                    <td height='25' style='font-size: 25px; line-height: 25px;'>&nbsp;</td>" +
        "                                </tr>" +

        "                                <tr>" +
        "                                    <td align='left' style='color: #888888; font-size: 14px; font-family: 'Work Sans', Calibri, sans-serif; line-height: 23px;' class='text_color'>" +
        "                                        <div style='color: #333333; font-size: 14px; font-family: 'Work Sans', Calibri, sans-serif; font-weight: 600; mso-line-height-rule: exactly; line-height: 23px;'>" +

        "                                            Correo Electrórnico: <br> <a href='mailto:' style='color: #888888; font-size: 14px; font-family: 'Hind Siliguri', Calibri, Sans-serif; font-weight: 400;'>fun4fund@gmail.com</a>" +

        "                                        </div>" +
        "                                    </td>" +
        "                                </tr>" +

        "                            </tbody></table>" +

        "                            <table border='0' width='2' align='left' cellpadding='0' cellspacing='0' style='border-collapse:collapse; mso-table-lspace:0pt; mso-table-rspace:0pt;' class='container590'>" +
        "                                <tbody><tr>" +
        "                                    <td width='2' height='10' style='font-size: 10px; line-height: 10px;'></td>" +
        "                                </tr>" +
        "                            </tbody></table>" +

        "                            <table border='0' width='200' align='right' cellpadding='0' cellspacing='0' style='border-collapse:collapse; mso-table-lspace:0pt; mso-table-rspace:0pt;' class='container590'>" +

        "                                <tbody><tr>" +
        "                                    <td class='hide' height='45' style='font-size: 45px; line-height: 45px;'>&nbsp;</td>" +
        "                                </tr>" +



        "                                <tr>" +
        "                                    <td height='15' style='font-size: 15px; line-height: 15px;'>&nbsp;</td>" +
        "                                </tr>" +
        "                            </tbody></table>" +
        "                        </td>" +
        "                    </tr>" +
        "                </tbody></table>" +
        "            </td>" +
        "        </tr>" +

        "        <tr>" +
        "            <td height='60' style='font-size: 60px; line-height: 60px;'>&nbsp;</td>" +
        "        </tr>" +

        "    </tbody></table>" +
        "    <!-- end section -->" +

        "    <!-- footer ====== -->" +
        "    <table border='0' width='100%' cellpadding='0' cellspacing='0' bgcolor='f4f4f4'>" +

        "        <tbody><tr>" +
        "            <td height='25' style='font-size: 25px; line-height: 25px;'>&nbsp;</td>" +
        "        </tr>" +

        "        <tr>" +
        "            <td align='center'>" +

        "                <table border='0' align='center' width='590' cellpadding='0' cellspacing='0' class='container590'>" +

        "                    <tbody><tr>" +
        "                        <td>" +
        "                            <table border='0' align='left' cellpadding='0' cellspacing='0' style='border-collapse:collapse; mso-table-lspace:0pt; mso-table-rspace:0pt;' class='container590'>" +
        "                                <tbody><tr>" +
        "                                    <td align='left' style='color: #aaaaaa; font-size: 14px; font-family: 'Work Sans', Calibri, sans-serif; line-height: 24px;'>" +
        "                                        <div style='line-height: 24px;'>" +

        "                                            <span style='color: #333333;'>Grupo Logical Growth</span>" +

        "                                        </div>" +
        "                                    </td>" +
        "                                </tr>" +
        "                            </tbody></table>" +

        "                            <table border='0' align='left' width='5' cellpadding='0' cellspacing='0' style='border-collapse:collapse; mso-table-lspace:0pt; mso-table-rspace:0pt;' class='container590'>" +
        "                                <tbody><tr>" +
        "                                    <td height='20' width='5' style='font-size: 20px; line-height: 20px;'>&nbsp;</td>" +
        "                                </tr>" +
        "                            </tbody></table>" +
        "                        </td>" +
        "                    </tr>" +

        "                </tbody></table>" +
        "            </td>" +
        "        </tr>" +

        "        <tr>" +
        "            <td height='25' style='font-size: 25px; line-height: 25px;'>&nbsp;</td>" +
        "        </tr>" +

        "    </tbody></table>" +
        "</body>";

    public MailService(JHipsterProperties jHipsterProperties, JavaMailSender javaMailSender,
            MessageSource messageSource, SpringTemplateEngine templateEngine) {

        this.jHipsterProperties = jHipsterProperties;
        this.javaMailSender = javaMailSender;
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
    }

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
            isMultipart, isHtml, to, subject, content);
        isHtml =  true;
        content = template.replace("{{CONTENT}}", content);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom(jHipsterProperties.getMail().getFrom());
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent email to User '{}'", to);
        }  catch (MailException | MessagingException e) {
            log.warn("Email could not be sent to user '{}'", to, e);
        }
    }

    @Async
    public void sendEmailFromTemplate(User user, String templateName, String titleKey) {
        if (user.getEmail() == null) {
            log.debug("Email doesn't exist for user '{}'", user.getLogin());
            return;
        }
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(titleKey, null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendActivationEmail(User user) {
        log.debug("Sending activation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/activationEmail", "email.activation.title");
    }

    @Async
    public void sendCreationEmail(User user) {
        log.debug("Sending creation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/creationEmail", "email.activation.title");
    }

    @Async
    public void sendPasswordResetMail(User user) {
        log.debug("Sending password reset email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/passwordResetEmail", "email.reset.title");
    }
}
