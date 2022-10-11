
package com.forgotPassword.forgotPassword.controlador;

import com.forgotPassword.forgotPassword.Exception.CustomerNotFoundException;
import com.forgotPassword.forgotPassword.entidades.Usuario;
import com.forgotPassword.forgotPassword.servicio.CustomerServices;
import io.lettuce.core.dynamic.annotation.Param;
import java.io.UnsupportedEncodingException;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class ForgotPasswordController {
    
    @Autowired
    private JavaMailSender mailSender;
     
    @Autowired
    private CustomerServices customerService;
     
    @GetMapping("/forgot_password")

    public String showForgotPasswordForm(Model model) {
        model.addAttribute("pageTitle", "Forgot Password");
            return "index";
 
    }
 
    @PostMapping("/forgot_password")

 public String processForgotPassword(HttpServletRequest request, Model model) {
    String email = request.getParameter("email");
    String token = RandomString.make(30);
     
    try {
        customerService.updateResetPasswordToken(token, email);
        String resetPasswordLink = Utility.getSiteURL(request) + "/reset_password?token=" + token;
        sendEmail(email, resetPasswordLink);
        model.addAttribute("message", "We have sent a reset password link to your email. Please check.");
         
    } catch (CustomerNotFoundException ex) {
        model.addAttribute("error", ex.getMessage());
    } catch (UnsupportedEncodingException | MessagingException e) {
        model.addAttribute("error", "Error while sending email");
    }
         
    return "index";
}
     
    public void sendEmail(String recipientEmail, String link) throws MessagingException, UnsupportedEncodingException {
            MimeMessage message = mailSender.createMimeMessage();              
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setFrom("contact@shopme.com", "Shopme Support");
            helper.setTo(recipientEmail);

            String subject = "Here's the link to reset your password";

            String content = "<p>Hello,</p>"
                    + "<p>You have requested to reset your password.</p>"
                    + "<p>Click the link below to change your password:</p>"
                    + "<p><a href=\"" + link + "\">Change my password</a></p>"
                    + "<br>"
                    + "<p>Ignore this email if you do remember your password, "
                    + "or you have not made the request.</p>";

            helper.setSubject(subject);

            helper.setText(content, true);

            mailSender.send(message);
        }
 
    
     
     
    @GetMapping("/reset_password")

    public String showResetPasswordForm(@Param(value = "token") String token, Model model) {
        Usuario customer = customerService.getByResetPasswordToken(token);
        model.addAttribute("token", token);

        if (customer == null) {
            model.addAttribute("message", "Invalid Token");
            return "message";
        }
     
                return "reset_password_form";
            }
    
    
    @PostMapping("/reset_password")

    public String processResetPassword(HttpServletRequest request, Model model) {
    String token = request.getParameter("token");
    String password = request.getParameter("password");
     
    Usuario customer = customerService.getByResetPasswordToken(token);
    model.addAttribute("title", "Reset your password");
     
    if (customer == null) {
        model.addAttribute("message", "Invalid Token");
        return "message";
    } else {           
        customerService.updatePassword(customer, password);
         
        model.addAttribute("message", "You have successfully changed your password.");
    }
     
            return "index";
        }
 

 
    
    
}
