package com.admin.service.implementation;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.admin.bean.LoginBean;
import com.admin.bean.RegistrationBean;
import com.admin.constants.CommonConstants;
import com.admin.entity.OTPEntity;
import com.admin.entity.RegistrationForm;
import com.admin.exception.EmailAlreadyExistsException;
import com.admin.exception.EmailNotFoundException;
import com.admin.exception.InvalidOtpException;
import com.admin.exception.PasswordMismatchException;
import com.admin.repository.OtpRepository;
import com.admin.repository.RegistrationRepository;
import com.admin.service.RegistrationService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Implementation of the RegistrationService interface.
 */
@Service
public class RegistrationServiceImpl implements RegistrationService {

    @Autowired
    RegistrationRepository registrationRepository;
    @Autowired
    OtpRepository otpRepository;
    private static Logger log = LoggerFactory.getLogger(RegistrationServiceImpl.class.getSimpleName());
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JavaMailSender javaMailSender;

    /**
     * Saves the registration details.
     *
     * @param registrationBean The registration details to be saved.
     * @return The saved registration details.
     * @throws EmailAlreadyExistsException If the email already exists.
     */
    @Override
    public RegistrationBean saveRegistration(RegistrationBean registrationBean) {
        log.info("Saving the registration details");

        if (registrationRepository.existsByEmail(registrationBean.getEmail())) {
            log.info("Email ID already exists: {}", registrationBean.getEmail());
            throw new EmailAlreadyExistsException("The Email ID already exists");
        } else {
            RegistrationForm registrationEntity = objectMapper.convertValue(registrationBean, RegistrationForm.class);

            registrationRepository.save(registrationEntity);
            log.info("Registration details saved successfully");
            return registrationBean;
        }
    }

    /**
     * Validates the login using email and password.
     *
     * @param loginBean The login details.
     * @return The user details if login is successful.
     * @throws PasswordMismatchException If the password does not match.
     * @throws EmailNotFoundException    If the email is not found.
     */
    @Override
    public RegistrationForm validateLogin(LoginBean loginBean) {
        RegistrationForm user = registrationRepository.findByEmail(loginBean.getEmail());
        log.info("login by using email and password");

        if (user != null) {
            log.info("email is valid");

            if (user.getPassword().equals(loginBean.getPassword())) {
                log.info("login successful");
                return user;
            } else {
                try {
                    log.info("login failed");
                    throw new PasswordMismatchException("Password is wrong");
                } catch (PasswordMismatchException exception) {
                    log.error("password is mismatch");
                }
            }
        } else {
            throw new EmailNotFoundException("Email not found");
        }
        return user;
    }

    /**
     * Generates a random OTP.
     *
     * @return The generated OTP.
     */
    @Override
    public String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    /**
     * Sends an OTP email.
     *
     * @param toEmail The recipient email address.
     * @param otp     The OTP to be sent.
     */
    @Override
    public void sendOtpEmail(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(CommonConstants.SUBJECT);
        message.setText(CommonConstants.TEXT + otp);

        javaMailSender.send(message);
    }

    /**
     * Initiates the process for resetting the password.
     *
     * @param email The email for which the password needs to be reset.
     * @return The user details for the given email.
     * @throws EmailNotFoundException If the email is not found.
     */
    @Override
    public RegistrationForm forgetPassword(String email) {
        log.info("Checking if email is present or not");
        RegistrationForm user = registrationRepository.findByEmail(email);

        if (user != null) {
            log.info("Email is valid");
            String otp = generateOtp();

            Timestamp expirationTime = Timestamp.from(Instant.now().plus(Duration.ofMinutes(5)));

            sendOtpEmail(email, otp);
            saveOtp(email, otp, expirationTime);
            return user;
        } else {
            log.info("Email is not valid");
            throw new EmailNotFoundException("Email not found");
        }
    }

    /**
     * Saves the OTP along with its expiration time.
     *
     * @param email          The email to which the OTP is sent.
     * @param otp            The OTP.
     * @param expirationTime The expiration time of the OTP.
     */
    @Override
    public void saveOtp(String email, String otp, Timestamp expirationTime) {
        Optional<OTPEntity> existingOtp = otpRepository.findByEmail(email);

        if (existingOtp.isPresent()) {
            existingOtp.get().setOtp(otp);
            existingOtp.get().setExpirationTime(expirationTime);
            otpRepository.save(existingOtp.get());
        } else {
            OTPEntity newOtp = new OTPEntity();
            newOtp.setEmail(email);
            newOtp.setOtp(otp);
            newOtp.setExpirationTime(expirationTime);
            otpRepository.save(newOtp);
        }
    }

    /**
     * Verifies the entered OTP.
     *
     * @param email      The email for which the OTP is sent.
     * @param enteredOtp The OTP entered by the user.
     * @return true if the OTP is valid, false otherwise.
     * @throws InvalidOtpException If the entered OTP is invalid.
     */
    @Override
    public boolean verifyOtp(String email, String enteredOtp) {
        OTPEntity otpEntity = otpRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("OTP not found"));

        Timestamp expirationTime = otpEntity.getExpirationTime();

        LocalDateTime expirationLocalDateTime = expirationTime.toInstant().atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        if (expirationLocalDateTime.isBefore(LocalDateTime.now())) {
            return false;
        } else if (!enteredOtp.equals(otpEntity.getOtp())) {
            throw new InvalidOtpException("Entered Correct OTP");
        } else {
            return true;
        }
    }

    /**
     * Updates the password for the given email.
     *
     * @param email    The email for which the password needs to be updated.
     * @param password The new password.
     * @return The updated user details.
     * @throws EmailNotFoundException If the email is not found.
     */
    @Override
    public RegistrationForm updatePassword(String email, String password) {
        RegistrationForm result = registrationRepository.findByEmail(email);
        if (result != null) {
            result.setPassword(password);
            registrationRepository.save(result);
            return result;
        } else {
            throw new EmailNotFoundException("Given email is not found");
        }
    }

    /**
     * Scheduled task to cleanup expired OTPs.
     */
    @Scheduled(fixedRate = 300000)
    public void cleanupExpiredOtps() {
        try {
            otpRepository.deleteExpiredOtps();
        } catch (Exception e) {
            log.error("Error occurred while cleaning up expired OTPs: " + e.getMessage(), e);
        }
    }
}

