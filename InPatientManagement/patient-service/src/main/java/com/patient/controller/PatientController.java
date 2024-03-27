/**
 * Controller class for managing patient-related operations.
 */
package com.patient.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.patient.bean.PatientBean;
import com.patient.entity.PatientEntity;
import com.patient.exception.DoctorNameNotFoundException;
import com.patient.exception.PatientDetailsNotFoundException;
import com.patient.exception.PatientIdNotFoundException;
import com.patient.service.PatientService;

@RestController
@RequestMapping("/patients")
@CrossOrigin(origins = "**")

public class PatientController {
    private static final Logger log = LoggerFactory.getLogger(PatientController.class);

    @Autowired
    private PatientService patientService;

    /**
     * Endpoint to save patient details.
     * @param patientBean The patient details to be saved.
     * @return ResponseEntity with the saved patient details and HTTP status OK if successful, otherwise INTERNAL_SERVER_ERROR.
     */
    @PostMapping("/save")
    public ResponseEntity<PatientBean> savePatientDetails(@RequestBody PatientBean patientBean) {
        try {
            log.info("Saving patient: " + patientBean.toString());
            PatientBean savedPatient = patientService.savePatientDetails(patientBean);
            return new ResponseEntity<>(savedPatient, HttpStatus.OK);
        } catch (Exception e) {
            log.error("An error occurred while saving patient: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint to fetch all patient details.
     * @return ResponseEntity with a list of patient details and HTTP status OK if successful, otherwise INTERNAL_SERVER_ERROR.
     */
    @GetMapping
    public ResponseEntity<List<PatientBean>> getAllPatientDetails() {
        try {
            log.info("Fetching all patients");
            List<PatientBean> patientBeans = patientService.getAllPatientDetails();
            return new ResponseEntity<>(patientBeans, HttpStatus.OK);
        } catch (PatientDetailsNotFoundException e) {
            log.error("An error occurred while fetching all patients: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint to fetch patient details by ID.
     * @param id The ID of the patient.
     * @return ResponseEntity with the patient details and HTTP status OK if successful, otherwise NOT_FOUND or INTERNAL_SERVER_ERROR.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PatientEntity> getPatientById(@PathVariable int id) {
        try {
            log.info("Fetching patient by ID: " + id);
            Optional<PatientEntity> patientEntity = patientService.getPatientById(id);
            return patientEntity.map(entity -> new ResponseEntity<>(entity, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (PatientIdNotFoundException e) {
            log.error("An error occurred while fetching patient by ID: " + id + ". Error: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint to fetch patients by doctor name.
     * @param name The name of the doctor.
     * @return ResponseEntity with a list of patient details and HTTP status OK if successful, otherwise INTERNAL_SERVER_ERROR.
     */
    @GetMapping("/getByDoctor/{name}")
    public ResponseEntity<?> getPatientsByDoctorName(@PathVariable String name) {
        try {
            log.info("Fetching patients by doctor: " + name);
            List<Object[]> result = patientService.getPatientDetailsByDoctor(name);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (DoctorNameNotFoundException e) {
            log.error("An error occurred while fetching patients by doctor: " + name + ". Error: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint to fetch patients by full name.
     * @param name The full name of the patient.
     * @return ResponseEntity with a list of patient details and HTTP status OK if successful, otherwise INTERNAL_SERVER_ERROR.
     */
    @GetMapping("/getByName/{name}")
    public ResponseEntity<?> getPatientsByName(@PathVariable String name) {
        try {
            log.info("Fetching patients by full name: " + name);
            List<Object[]> result = patientService.getPatientDetailsByFullName(name);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            log.error("An error occurred while fetching patients by full name: " + name + ". Error: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint to generate a patient number.
     * @return ResponseEntity with the generated patient number and HTTP status OK if successful, otherwise INTERNAL_SERVER_ERROR.
     */
    @GetMapping("/generate-number")
    public ResponseEntity<String> generatePatientNumber() {
        try {
            log.info("Generating patient number");
            String patientNumber = patientService.generatePatientNo();
            return new ResponseEntity<>(patientNumber, HttpStatus.OK);
        } catch (Exception e) {
            log.error("An error occurred while generating patient number: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
