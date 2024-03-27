/**
 * This class represents the REST controller for managing bed allocations.
 */
package com.admin.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.admin.bean.BedAllocationBean;
import com.admin.bean.PatientBean;
import com.admin.dto.BedAllocationDto;
import com.admin.entity.BedAllocation;
import com.admin.service.BedAllocationService;

@RestController
@RequestMapping(path = "bedAllocation")
@CrossOrigin(origins = "**")

public class BedAllocationController {

    @Autowired
    BedAllocationService bedAllocationService;
    private static Logger log = LoggerFactory.getLogger(BedAllocationController.class.getSimpleName());

    /**
     * Endpoint for saving bed allocation details.
     */
    @PostMapping("/save")
    public ResponseEntity<BedAllocationBean> saveBedAllocationDetails(
            @RequestBody BedAllocationBean bedAllocationBean) {
        log.info("Saving Bed Allocation");

        BedAllocationBean bedAllocation1 = bedAllocationService.save(bedAllocationBean);
        ResponseEntity<BedAllocationBean> responseEntity = new ResponseEntity<>(bedAllocation1, HttpStatus.CREATED);
        log.info("Saved Bed Allocation details successfully");
        return responseEntity;
    }

    /**
     * Endpoint for retrieving patient details by patient ID.
     */
    @GetMapping("/getPatientById/{id}")
    public ResponseEntity<PatientBean> getPatientDetailsByPatientId(@PathVariable(value = "id") Integer patientId) {
        log.info("Retrieving Patient Details by patient ID");

        PatientBean patientBillingBean = bedAllocationService.getDetails(patientId);
        log.info("Retrieved patient details by patient ID successfully");
        return new ResponseEntity<PatientBean>(patientBillingBean, HttpStatus.OK);
    }

    /**
     * Endpoint for retrieving bed allocation details by ID.
     */
    @GetMapping("/getById/{id}")
    public ResponseEntity<BedAllocationBean> getBedAllocationDetailsById(@PathVariable int id) {
        BedAllocationBean bedAllocation = bedAllocationService.getById(id);
        log.info("Retrieved bed allocation details by ID successfully");
        return new ResponseEntity<BedAllocationBean>(bedAllocation, HttpStatus.OK);
    }

    /**
     * Endpoint for retrieving all bed allocation details.
     */
    @GetMapping("/getAll")
    public ResponseEntity<List<BedAllocationBean>> getAllBedAllocationDetails() {
        log.info("Retrieving All Bed Allocation Details");

        List<BedAllocationBean> list = bedAllocationService.getAll();
        ResponseEntity<List<BedAllocationBean>> responseEntity = new ResponseEntity<>(list, HttpStatus.OK);
        log.info("Retrieving All Bed Allocation Details is done");
        return responseEntity;
    }

    /**
     * Endpoint for retrieving bed allocation by patient number.
     */
    @GetMapping("/getByPatientNumber/{no}")
    public ResponseEntity<BedAllocation> getByPatientNumber(@PathVariable String no) {
        log.info("Retrieving Bed Allocation by using patient number");

        BedAllocation bedAllocation = bedAllocationService.getDetailsForUpdating(no);
        log.info("Retrieved Bed Allocation by using patient number successfully");
        return new ResponseEntity<BedAllocation>(bedAllocation, HttpStatus.OK);
    }

    /**
     * Endpoint for deleting bed allocation by ID.
     */
    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<String> deleteById(@PathVariable int id) {
        log.info("Deleting Bed Allocation by ID");

        bedAllocationService.getById(id);
        bedAllocationService.delete(id);
        ResponseEntity<String> responseEntity = new ResponseEntity<>("Deleted Successfully", HttpStatus.OK);
        log.info("Deleting Bed Allocation by ID is done");
        return responseEntity;
    }

    /**
     * Endpoint for updating bed allocation details.
     */
    @PutMapping("/update")
    public ResponseEntity<String> updateBedAllocationDetails(@RequestBody BedAllocationBean bedAllocationBean)
            throws Exception {

        log.info("Updating Bed Allocation");

        bedAllocationService.update(bedAllocationBean);
        ResponseEntity<String> responseEntity = new ResponseEntity<>("Updated successfully", HttpStatus.OK);
        log.info("Updating Bed Allocation is done");
        return responseEntity;
    }

    /**
     * Endpoint for retrieving bed allocation details.
     */
    @GetMapping("/bed")
    public ResponseEntity<List<BedAllocationDto>> getDetails() {
        log.info("Getting the Bed Allocation details");

        List<BedAllocationDto> bedDetails = bedAllocationService.getBedDetails();
        log.info("Getting the Bed Allocation details is done");
        return new ResponseEntity<List<BedAllocationDto>>(bedDetails, HttpStatus.OK);
    }
}
