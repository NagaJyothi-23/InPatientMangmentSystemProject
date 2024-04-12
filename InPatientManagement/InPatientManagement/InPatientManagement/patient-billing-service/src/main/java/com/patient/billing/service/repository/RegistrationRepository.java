package com.patient.billing.service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.patient.billing.service.entity.RegistrationForm;

@Repository
@EnableJpaRepositories
public interface RegistrationRepository extends JpaRepository<RegistrationForm, Integer> {
	Optional<RegistrationForm> findByEmail(String email);

	boolean existsByEmail(String email);

}