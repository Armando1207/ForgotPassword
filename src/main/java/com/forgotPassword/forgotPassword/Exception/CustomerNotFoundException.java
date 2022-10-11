
package com.forgotPassword.forgotPassword.Exception;

import lombok.Data;

public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException() {
        super();
    }

    public CustomerNotFoundException(String message) {
        super(message);
    }
  
}
