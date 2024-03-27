/**
 * Controller class responsible for handling HTTP requests related to wards.
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

import com.admin.bean.WardBean;
import com.admin.entity.Ward;
import com.admin.service.WardService;

import lombok.extern.slf4j.Slf4j;

/**
 * Controller class for managing HTTP requests related to wards.
 */
@Slf4j
@RestController
@RequestMapping("/ward")
@CrossOrigin(origins = "**")

public class WardController {
    
    Logger log = LoggerFactory.getLogger(WardController.class);

    @Autowired
    private WardService wardService;

    /**
     * Endpoint to save ward details.
     * @param wardBean The ward details to be saved.
     * @return ResponseEntity with the saved ward details and HTTP status.
     */
    @PostMapping("/save")
    public ResponseEntity<WardBean> saveWard(@RequestBody WardBean wardBean) {
        log.info("Started WardController::save()");
        WardBean wardBean1 = wardService.saveWard(wardBean);
        log.info("Ward saved successfully");
        return new ResponseEntity<WardBean>(wardBean1, HttpStatus.OK);
    }

    /**
     * Endpoint to retrieve ward details by ID.
     * @param id The ID of the ward.
     * @return ResponseEntity with the ward details and HTTP status.
     */
    @GetMapping("/getById/{id}")
    public ResponseEntity<WardBean> getWardById(@PathVariable Long id) {
        log.info("Start WardController:getById");
        WardBean wardBean = wardService.getByWardId(id);
        log.info("Ward fetched by Id successfully");
        return new ResponseEntity<WardBean>(wardBean, HttpStatus.OK);
    }

    /**
     * Endpoint to retrieve all ward details.
     * @return ResponseEntity with the list of ward details and HTTP status.
     */
    @GetMapping("/getAll")
    public ResponseEntity<List<WardBean>> getAllWards() {
        log.info("Retrieving all wards");
        List<WardBean> wardBean = wardService.getAllWards();
        log.info("Retrieving all wards successfully");
        return new ResponseEntity<List<WardBean>>(wardBean, HttpStatus.OK);
    }

    /**
     * Endpoint to retrieve wards by department ID.
     * @param id The ID of the department.
     * @return ResponseEntity with the list of wards and HTTP status.
     */
    @GetMapping("/getByDepartmentId/{id}")
    public ResponseEntity<List<Ward>> getWardsByDepartmentId(@PathVariable Long id) {
        log.info("Getting the ward details successfully");
        List<Ward> ward = wardService.findByDepartmentId(id);
        return new ResponseEntity<List<Ward>>(ward, HttpStatus.OK);
    }

    /**
     * Endpoint to update ward details by ID.
     * @param wardBean The updated ward details.
     * @param id The ID of the ward to be updated.
     * @return ResponseEntity with HTTP status.
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateWardById(@RequestBody WardBean wardBean, @PathVariable Long id) {
        log.info("Start::WardController::update");
        wardService.updateWard(wardBean);
        log.info("Ward updated successfully");
        return new ResponseEntity<>("Update successfully" + wardBean, HttpStatus.OK);
    }

    /**
     * Endpoint to delete ward by ID.
     * @param id The ID of the ward to be deleted.
     * @return ResponseEntity with HTTP status.
     */
    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<Void> deleteWardById(@PathVariable Long id) {
        log.info("Start WardController:delete()");
        wardService.delete(id);
        log.info("Deleted successfully");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Endpoint to update the status of ward.
     * @param ward The ward entity containing the updated status.
     */
    @PutMapping("/updateStatus")
    public void updateWardStatus(@RequestBody Ward ward) {
        log.info("Updating the status of ward");
        wardService.updateStatus(ward);
        log.info("Updated ward status successfully");
    }
}
