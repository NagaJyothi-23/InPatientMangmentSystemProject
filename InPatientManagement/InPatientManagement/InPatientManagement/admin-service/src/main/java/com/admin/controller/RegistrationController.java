package com.admin.controller;


import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.admin.bean.RegistrationBean;
import com.admin.dto.AuthRequest;
import com.admin.dto.JwtResponse;
import com.admin.entity.RefreshToken;
import com.admin.entity.RegistrationForm;
import com.admin.exception.UsernameNotFoundException;
import com.admin.repository.RegistrationRepository;
import com.admin.service.RegistrationService;
import com.admin.service.implementation.JwtService;
import com.admin.service.implementation.RefreshTokenService;

@RestController
@RequestMapping("/register")
@CrossOrigin(origins = "*")
public class RegistrationController {

	@Autowired
	private RegistrationService registrationService;
	@Autowired
	private JwtService jwtService;

	@Autowired
	private RefreshTokenService refreshTokenService;

	@Autowired
	private AuthenticationManager authenticationManager;
	 @Autowired
	    RegistrationRepository registrationRepository;

	private static Logger log = LoggerFactory.getLogger(RegistrationController.class.getSimpleName());

	@PostMapping("/save")
	public ResponseEntity<RegistrationBean> saveRegistrationDetails(@RequestBody RegistrationBean registrationBean) {
		log.info("Saving Registration entity");

		registrationService.saveRegistration(registrationBean);
		ResponseEntity<RegistrationBean> responseEntity = new ResponseEntity<>(registrationBean, HttpStatus.CREATED);
		log.info("Saving Registration entity is done");
		return responseEntity;

	}

//	 @PostMapping("/login")
//	public ResponseEntity<RegistrationForm> login(@RequestBody LoginBean loginBean) {
//		log.info("checking email is present or not");
//
//		RegistrationForm user = registrationService.validateLogin(loginBean);
//
//		if (user != null) {
//
//			log.info("login sucessfull");
//			return new ResponseEntity<RegistrationForm>(user, HttpStatus.OK);
//		} else {
//			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
//		}
//
//	}

	@GetMapping("/generateOtp")
	public ResponseEntity<RegistrationForm> generateOtpAndSendEmail(@RequestParam("email") String email) {

		log.info("Generate otp by using email");
		RegistrationForm user = registrationService.forgetPassword(email);
		if (user != null) {
			log.info("Generate otp by using email is done");
			return new ResponseEntity<RegistrationForm>(user, HttpStatus.OK);
		} else {

			return new ResponseEntity<RegistrationForm>(HttpStatus.UNAUTHORIZED);
		}

	}

	@PostMapping("/verify")
	public ResponseEntity<String> verifyOtp(@RequestParam String email, @RequestParam String enteredOtp) {

		log.info("verify the otp by using email");
		if (registrationService.verifyOtp(email, enteredOtp)) {
			String jsonString = "{\"message\":\"Verified Successfully\"}";
			log.info("verify the  otp by using email is done");

			return ResponseEntity.status(HttpStatus.OK).header("Content-Type", "application/json").body(jsonString);
		} else {
			log.info("Sending  the invalid otp");
			String jsonString = "{\"message\":\"Invalid OTP\"}";
			System.out.println("jsonString" + jsonString);

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Content-Type", "application/json")
					.body(jsonString);
		}

	}

	@PutMapping("/password")
	public ResponseEntity<String> updatePassword(@RequestParam String email, @RequestParam String password) {

		log.info("Update the password");

		registrationService.updatePassword(email, password);
		log.info("Updated the  password successfully");
		return new ResponseEntity<String>("updated successfully", HttpStatus.OK);

	}

	@PostMapping("/login")
	public JwtResponse authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
		if (authentication.isAuthenticated()) {
			
//			RegistrationForm user = registrationService.validateLogin(authRequest);
			RegistrationForm user = registrationRepository.findByEmail(authRequest.getEmail()).get();
        
			RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequest.getEmail());
			return JwtResponse.builder().accessToken(jwtService.generateToken(authRequest.getEmail()))
					.token(refreshToken.getToken()).registrationForm(user).build();
		} else {
			throw new UsernameNotFoundException("invalid user request !");
		}
	}

	@PostMapping("/refreshToken")
	public JwtResponse refreshToken(@RequestBody RefreshToken refreshTokenRequest) {
		return refreshTokenService.findByToken(refreshTokenRequest.getToken())
				.map(refreshTokenService::verifyExpiration).map(RefreshToken::getRegistrationForm).map(userInfo -> {
					String accessToken = jwtService.generateToken(userInfo.getFirstName());
					return JwtResponse.builder().accessToken(accessToken).token(refreshTokenRequest.getToken()).build();
				}).orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));
	}

}