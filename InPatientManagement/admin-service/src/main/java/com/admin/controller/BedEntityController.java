/**
 * This class defines REST API endpoints for managing beds.
 */
package com.admin.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.admin.bean.BedBean;
import com.admin.service.BedService;

@Controller
@RequestMapping("/bed")
@CrossOrigin(origins = "**")

public class BedEntityController {

    @Autowired
    BedService bedService;
    private static Logger log = LoggerFactory.getLogger(BedAllocationController.class.getSimpleName());

    /**
     * Endpoint to save a new bed.
     *
     * @param bedBean The bed details to be saved.
     * @return ResponseEntity with the saved bed details.
     */
    @PostMapping("/save")
    public ResponseEntity<BedBean> saveBed(@RequestBody BedBean bedBean) {
        log.info("Saving Bed");
        BedBean bedDetails = bedService.save(bedBean);
        ResponseEntity<BedBean> responseEntity = new ResponseEntity<>(bedDetails, HttpStatus.CREATED);
        log.info("Bed saved successfully");
        return responseEntity;
    }

    /**
     * Endpoint to retrieve bed details by ID.
     *
     * @param bedId The ID of the bed to retrieve.
     * @return ResponseEntity with the retrieved bed details.
     */
    @GetMapping("/getById/{bedId}")
    public ResponseEntity<BedBean> getBedDetailsById(@PathVariable Long bedId) {
        log.info("Getting Bed Details by ID");
        BedBean bed = bedService.getById(bedId);
        log.info("Retrieving bed details by ID is done");
        return new ResponseEntity<>(bed, HttpStatus.OK);
    }

    /**
     * Endpoint to retrieve all bed details.
     *
     * @return ResponseEntity with the list of all bed details.
     */
    @GetMapping("/getAll")
    public ResponseEntity<List<BedBean>> getAllBedDetails() {
        log.info("Retrieving all bed details");
        List<BedBean> list = bedService.getAll();
        ResponseEntity<List<BedBean>> responseEntity = new ResponseEntity<>(list, HttpStatus.OK);
        log.info("Retrieved all bed details successfully");
        return responseEntity;
    }

    /**
     * Endpoint to retrieve bed details by room ID.
     *
     * @param id The ID of the room to retrieve bed details.
     * @return ResponseEntity with the list of bed details belonging to the specified room.
     */
    @GetMapping(path = "/getByRoomId/{id}")
    public ResponseEntity<List<BedBean>> getAllByWard(@PathVariable Long id) {
        log.info("Retrieving bed details by room ID");
        List<BedBean> list = bedService.findByBedIdRoomEntityId(id);
        log.info("Retrieving bed details by room ID successfully");
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /**
     * Endpoint to update bed details.
     *
     * @param bedId The ID of the bed to update.
     * @param bed    The updated bed details.
     * @return ResponseEntity indicating the success of the update operation.
     */
    @PutMapping("/update/{bedId}")
    public ResponseEntity<String> updateBedDetails(@PathVariable Long bedId, @RequestBody BedBean bed) {
        log.info("Updating Bed Status");
        bedService.update(bedId, bed);
        ResponseEntity<String> responseEntity = new ResponseEntity<>("Bed Status updated successfully", HttpStatus.OK);
        log.info("Updated bed successfully");
        return responseEntity;
    }

    /**
     * Endpoint to delete a bed by ID.
     *
     * @param bedId The ID of the bed to delete.
     * @return ResponseEntity indicating the success of the delete operation.
     */
    @DeleteMapping("/deleteById/{bedId}")
    public ResponseEntity<String> deleteBedById(@PathVariable Long bedId) {
        log.info("Deleting Bed by ID");
        bedService.getById(bedId);
        bedService.delete(bedId);
        ResponseEntity<String> responseEntity = new ResponseEntity<>("Deleted successfully", HttpStatus.OK);
        log.info("Deleted Bed successfully");
        return responseEntity;
    }
}
