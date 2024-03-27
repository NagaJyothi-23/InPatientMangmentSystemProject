/**
 * Controller class for handling patient billing operations.
 */
package com.patient.billing.service.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.patient.billing.service.dto.BedAllocationDto;
import com.patient.billing.service.dto.PatientBillingDTO;
import com.patient.billing.service.exception.BedAllocationNotFoundException;
import com.patient.billing.service.exception.BillingDetailsNotFoundException;
import com.patient.billing.service.exception.PatientBillingExceptionHandler;
import com.patient.billing.service.exception.PatientNumberNotFoundException;
import com.patient.billing.service.service.PatientBillingService;

@RestController
@RequestMapping(path = "/patientbilling")
@CrossOrigin(origins = "**")


public class PatientBillingController {

    @Autowired
    private PatientBillingService patientBillingService;
    @Autowired
    private PatientBillingExceptionHandler exceptionHandler;
    private static Logger log = LoggerFactory.getLogger(PatientBillingController.class.getSimpleName());

    /**
     * Endpoint to save billing details.
     * @param patientBillingBean The billing details to be saved.
     * @return ResponseEntity with the saved billing details and HTTP status OK.
     */
    @PostMapping(path = "/save")
    public ResponseEntity<BedAllocationDto> saveBillingDetails(@RequestBody BedAllocationDto patientBillingBean) {
        log.info("saving billing details " + patientBillingBean);
        patientBillingService.savebillingDetails(patientBillingBean);
        log.info("saving billing details successfully " + patientBillingBean);
        return new ResponseEntity<>(patientBillingBean, HttpStatus.OK);
    }

    /**
     * Endpoint to retrieve all billing details.
     * @return ResponseEntity with the list of billing details and HTTP status OK if found, otherwise HTTP status NOT_FOUND.
     */
    @GetMapping(path = "/get")
    public ResponseEntity<List<PatientBillingDTO>> getAllBillingDetails() {
        log.info("Calling getAllDetails method in BillingController");
        try {
            Optional<List<PatientBillingDTO>> billingDetails = patientBillingService.getAllBillingDetails();

            if (billingDetails.isPresent()) {
                log.info("Received billing details successfully");
                return new ResponseEntity<>(billingDetails.get(), HttpStatus.OK);
            } else {
                log.info("Billing details not found");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (BillingDetailsNotFoundException e) {
            log.error("Billing details not found");
            exceptionHandler.billingDetailsNotFoundException(e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint to filter billing details based on date range.
     * @param startDate Start date of the date range.
     * @param endDate End date of the date range (optional, defaults to current date if not provided).
     * @return ResponseEntity with the filtered billing details and HTTP status OK if found, otherwise HTTP status NOT_FOUND.
     */
    @GetMapping("/filter")
    public ResponseEntity<Optional<List<PatientBillingDTO>>> filterBillingDetailsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            log.info("getting the billing details based on start date and end date");
            if (endDate == null) {
                endDate = LocalDate.now();
            }
            Optional<List<PatientBillingDTO>> result = patientBillingService.filterBillingDetailsByDateRange(startDate,
                    endDate);
            log.info("get billing details based on start date and end date successfully");
            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (BillingDetailsNotFoundException e) {
            log.error("billing details not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint to retrieve bed allocation details based on patient number.
     * @param patientNumber The patient number.
     * @return ResponseEntity with the bed allocation details and HTTP status OK if found, otherwise HTTP status NOT_FOUND.
     */
    @GetMapping("/getno")
    public ResponseEntity<Optional<BedAllocationDto>> getBedAllocationDetails(@RequestParam String patientNumber) {
        try {
            log.error("getting details based on patient number");
            Optional<BedAllocationDto> bedDetails = patientBillingService
                    .getBedAllocationDetailsBasedOnPatientNumber(patientNumber);
            log.error("get the details based on patient number successfully");
            return new ResponseEntity<>(bedDetails, HttpStatus.OK);
        } catch (PatientNumberNotFoundException | BedAllocationNotFoundException e) {
            log.error("Patient number not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
