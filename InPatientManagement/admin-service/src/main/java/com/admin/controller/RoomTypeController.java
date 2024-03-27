/**
 * Controller class responsible for handling HTTP requests related to room types.
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

import com.admin.bean.RoomTypeBean;
import com.admin.entity.RoomType;
import com.admin.service.RoomTypeService;

@Controller
@RequestMapping("/roomType")
@CrossOrigin(origins ="**")
public class RoomTypeController {
    
    private static final Logger logger = LoggerFactory.getLogger(RoomController.class);
    
    @Autowired
    RoomTypeService roomTypeService;

    /**
     * Endpoint to save room type details.
     * @param roomTypeBean The room type details to be saved.
     * @return ResponseEntity with the saved room type details and HTTP status.
     */
    @PostMapping(path = "/save")
    public ResponseEntity<RoomTypeBean> saveRoomTypeDetails(@RequestBody RoomTypeBean roomTypeBean) {
        logger.info("Saving the room type details ");
        roomTypeService.save(roomTypeBean);
        ResponseEntity<RoomTypeBean> entity = new ResponseEntity<>(roomTypeBean, HttpStatus.CREATED);
        logger.info("Room type details saved successfully");
        return entity;
    }

    /**
     * Endpoint to retrieve all room type details.
     * @return ResponseEntity with the list of room type details and HTTP status.
     */
    @GetMapping(path = "/getAll")
    public ResponseEntity<List<RoomTypeBean>> getAllRoomTypes() {
        logger.info("Getting the room type details");
        List<RoomTypeBean> list = roomTypeService.getAll();
        logger.info("Get the room type details successfully");
        return new ResponseEntity<List<RoomTypeBean>>(list, HttpStatus.OK);
    }

    /**
     * Endpoint to retrieve room type details by ID.
     * @param id The ID of the room type.
     * @return ResponseEntity with the room type details and HTTP status.
     */
    @GetMapping(path = "/getById/{id}")
    public ResponseEntity<RoomTypeBean> getRoomTypeById(@PathVariable Long id) {
        logger.info("Getting the room type details by using id");
        RoomTypeBean roombyid = roomTypeService.getById(id);
        logger.info("Get the room type details by using id successfully");
        return new ResponseEntity<RoomTypeBean>(roombyid, HttpStatus.OK);
    }

    /**
     * Endpoint to delete room type by ID.
     * @param id The ID of the room type to be deleted.
     * @return ResponseEntity with HTTP status.
     */
    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<Void> deleteRoomTypeById(@PathVariable long id) {
        logger.info("Deleting the room type by using id");
        roomTypeService.delete(id);
        logger.info("Deleting the room type by using id is done");
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint to update room type details.
     * @param roomType The room type details to be updated.
     * @return ResponseEntity with HTTP status.
     */
    @PutMapping("/update")
    public ResponseEntity<Void> updateStatus(@RequestBody RoomTypeBean roomType) {
        logger.info("Updating the room type details");
        roomTypeService.updateRoomType(roomType);
        logger.info("Updated the room type details successfully");
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint to update the status of room type.
     * @param roomType The room type entity containing the updated status.
     * @return ResponseEntity with HTTP status.
     */
    @PutMapping(path = "/status")
    public ResponseEntity<Void> put(@RequestBody RoomType roomType) {
        logger.info("Updating the status of room type");
        roomTypeService.updateStatus(roomType);
        logger.info("Updated the status of room type successfully");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
