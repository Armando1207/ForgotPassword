
package com.forgotPassword.forgotPassword.servicio;

import com.forgotPassword.forgotPassword.Repositorio.CustomerRepository;
import com.forgotPassword.forgotPassword.entidades.Usuario;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class CustomerServices {
    @Autowired
    private CustomerRepository customerRepo;

    public void updateResetPasswordToken(String token, String email){
        Usuario customer = customerRepo.findByEmail(email);
        if (customer != null) {
            customer.setResetPasswordToken(token);
            customerRepo.save(customer);
        } else {
        }
    }
     
    public Usuario getByResetPasswordToken(String token) {
        return customerRepo.findByResetPasswordToken(token);
    }
     
    public void updatePassword(Usuario customer, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        customer.setPassword(encodedPassword);
         
        customer.setResetPasswordToken(null);
        customerRepo.save(customer);
    }
    
}
