/**
 * Controller class for handling doctor-related operations.
 */
package com.patient.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.patient.bean.DoctorBean;
import com.patient.entity.DoctorEntity;
import com.patient.exception.DoctorDetailsNotFoundException;
import com.patient.exception.DoctorIdNotFoundException;
import com.patient.service.DoctorService;

@Controller
@RequestMapping("/doctor")
@CrossOrigin(origins = "**")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;
    private static Logger log = LoggerFactory.getLogger(DoctorController.class.getSimpleName());

    /**
     * Endpoint to save doctor details.
     * @param doctorBean The doctor details to be saved.
     * @return ResponseEntity with the saved doctor details and HTTP status CREATED if successful, otherwise HTTP status NOT_FOUND.
     */
    @PostMapping("/save")
    public ResponseEntity<DoctorBean> saveDoctorDetails(@RequestBody DoctorBean doctorBean) {
        log.info("Saving Doctor");
        try {
            doctorBean = doctorService.saveDoctorDetails(doctorBean);
            ResponseEntity<DoctorBean> responseEntity = new ResponseEntity<>(doctorBean, HttpStatus.CREATED);
            log.info("Saving doctor is done");
            return responseEntity;
        } catch (Exception e) {
            log.error("error handled");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint to retrieve doctor details by ID.
     * @param id The ID of the doctor.
     * @return ResponseEntity with the doctor details and HTTP status OK if found, otherwise HTTP status INTERNAL_SERVER_ERROR.
     */
    @GetMapping("/getById/{id}")
    public ResponseEntity<DoctorBean> getDoctorById(@PathVariable long id) {
        log.info("Getting Doctor Details by Id");
        try {
            DoctorBean doctorBean = doctorService.getDoctorById(id);
            log.info("Getting Doctor Details by Id is done");
            return new ResponseEntity<>(doctorBean, HttpStatus.OK);
        } catch (DoctorIdNotFoundException e) {
            log.error("An error occurred while getting doctor by ID: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint to retrieve all doctor details.
     * @return ResponseEntity with the list of doctor details and HTTP status OK if found, otherwise HTTP status INTERNAL_SERVER_ERROR.
     */
    @GetMapping("/getAll")
    public ResponseEntity<List<DoctorBean>> getAllDoctorDetails() {
        log.info("Getting All Doctor Details");
        try {
            List<DoctorBean> list = doctorService.getAllDoctorDetails();
            log.info("Getting All Doctor Details is done");
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (DoctorDetailsNotFoundException e) {
            log.error("An error occurred while getting all doctors: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint to retrieve all doctor details with department names.
     * @return ResponseEntity with the list of doctor details and HTTP status OK if found, otherwise HTTP status INTERNAL_SERVER_ERROR.
     */
    @GetMapping("/getAllWithDept")
    public ResponseEntity<List<Object[]>> getDoctorsByDepartment() {
        log.info("Getting All Doctor Details with Department Names");
        try {
            List<Object[]> list = doctorService.getAllWithDept();
            log.info("Getting All Doctor Details with dept names is done");
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            log.error("An error occurred while getting all doctors with department names: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * Endpoint to update doctor details.
     * @param doctorBean The updated doctor details.
     * @return ResponseEntity with a success message and HTTP status OK if successful, otherwise HTTP status INTERNAL_SERVER_ERROR.
     */
    @PutMapping("/update")
    public ResponseEntity<String> updateDoctorDetails(@RequestBody DoctorBean doctorBean) {
        log.info("Updating Doctor");
        try {
            doctorService.updateDoctorDetails(doctorBean);
            log.info("Updating Doctor is done");
            return new ResponseEntity<>("Doctor updated successfully", HttpStatus.OK);
        } catch (DoctorDetailsNotFoundException e) {
            log.error("An error occurred while updating doctor: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint to update doctor status.
     * @param doctor The doctor entity with updated status.
     * @return ResponseEntity with a success message and HTTP status OK if successful, otherwise HTTP status INTERNAL_SERVER_ERROR.
     */
    @PutMapping("/updateStatus")
    public ResponseEntity<String> updateStatus(@RequestBody DoctorEntity doctor) {
        log.info("Updating Doctor Status");
        try {
            doctorService.updateStatus(doctor);
            log.info("Updating Doctor Status is done");
            return new ResponseEntity<>("Doctor status updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            log.error("An error occurred while updating doctor status: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
