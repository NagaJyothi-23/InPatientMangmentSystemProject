package com.admin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.admin.bean.LoginBean;
import com.admin.bean.RegistrationBean;
import com.admin.entity.RegistrationForm;
import com.admin.service.RegistrationService;

/**
 * Controller class to handle registration-related endpoints.
 */
@RestController
@RequestMapping("register")
@CrossOrigin(origins = "**")
public class RegistrationController {

    @Autowired
    RegistrationService registrationService;
    private static Logger log = LoggerFactory.getLogger(RegistrationController.class.getSimpleName());

    /**
     * Endpoint to save registration details.
     *
     * @param registrationBean The registration details to be saved.
     * @return ResponseEntity with the saved registration details.
     */
    @PostMapping("/save")
    public ResponseEntity<RegistrationBean> saveRegistrationDetails(@RequestBody RegistrationBean registrationBean) {
        log.info("Saving Registration entity");
        registrationService.saveRegistration(registrationBean);
        ResponseEntity<RegistrationBean> responseEntity = new ResponseEntity<>(registrationBean, HttpStatus.CREATED);
        log.info("Saving Registration entity is done");
        return responseEntity;
    }

    /**
     * Endpoint for user login.
     *
     * @param loginBean The login credentials.
     * @return ResponseEntity with user details if login is successful, else UNAUTHORIZED.
     */
    @PostMapping("/login")
    public ResponseEntity<RegistrationForm> login(@RequestBody LoginBean loginBean) {
        log.info("Checking if email is present or not");
        RegistrationForm user = registrationService.validateLogin(loginBean);
        if (user != null) {
            log.info("Login successful");
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    /**
     * Endpoint to generate OTP and send it to the user's email.
     *
     * @param email The email to which OTP will be sent.
     * @return ResponseEntity with user details if OTP generation is successful, else UNAUTHORIZED.
     */
    @GetMapping("/generateOtp")
    public ResponseEntity<RegistrationForm> generateOtpAndSendEmail(@RequestParam("email") String email) {
        log.info("Generate OTP by using email");
        RegistrationForm user = registrationService.forgetPassword(email);
        if (user != null) {
            log.info("Generate OTP by using email is done");
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Endpoint to verify OTP entered by the user.
     *
     * @param email      The email for which OTP was generated.
     * @param enteredOtp The OTP entered by the user.
     * @return ResponseEntity with success message if OTP is verified, else BAD_REQUEST.
     */
    @PostMapping("/verify")
    public ResponseEntity<String> verifyOtp(@RequestParam String email, @RequestParam String enteredOtp) {
        log.info("Verify the OTP by using email");
        if (registrationService.verifyOtp(email, enteredOtp)) {
            String jsonString = "{\"message\":\"Verified Successfully\"}";
            log.info("Verify the OTP by using email is done");
            return ResponseEntity.status(HttpStatus.OK).header("Content-Type", "application/json").body(jsonString);
        } else {
            log.info("Sending invalid OTP");
            String jsonString = "{\"message\":\"Invalid OTP\"}";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Content-Type", "application/json").body(jsonString);
        }
    }

    /**
     * Endpoint to update user password.
     *
     * @param email    The email for which password is to be updated.
     * @param password The new password.
     * @return ResponseEntity with success message.
     */
    @PutMapping("/password")
    public ResponseEntity<String> updatePassword(@RequestParam String email, @RequestParam String password) {
        log.info("Updating the password");
        registrationService.updatePassword(email, password);
        log.info("Updated the password successfully");
        return new ResponseEntity<>("Updated successfully", HttpStatus.OK);
    }
}
