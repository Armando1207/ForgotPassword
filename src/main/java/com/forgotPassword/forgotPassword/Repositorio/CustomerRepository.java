
package com.forgotPassword.forgotPassword.Repositorio;

import com.forgotPassword.forgotPassword.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Usuario, Integer>{
    
       
    //@Query("SELECT * FROM Customers  WHERE email = ?")
    public Usuario findByEmail(String email); 
     
    public Usuario findByResetPasswordToken(String token);
}
