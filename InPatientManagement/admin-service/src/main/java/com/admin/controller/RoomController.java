/**
 * This class defines REST API endpoints for managing rooms.
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

import com.admin.bean.RoomBean;
import com.admin.entity.RoomEntity;
import com.admin.repository.RoomRepository;
import com.admin.service.RoomService;

@RestController
@RequestMapping("/room")
@CrossOrigin(origins = "**")

public class RoomController {

    private static final Logger logger = LoggerFactory.getLogger(RoomController.class);

    @Autowired
    RoomService roomService;

    @Autowired
    RoomRepository roomRepository;

    /**
     * Endpoint to save a new room.
     *
     * @param roomBean The room details to be saved.
     * @return ResponseEntity with the saved room details.
     */
    @PostMapping(path = "/save")
    public ResponseEntity<RoomBean> savingRoom(@RequestBody RoomBean roomBean) {
        logger.info("Saving room");
        roomService.savingRoom(roomBean);
        logger.info("Room saved successfully");
        return new ResponseEntity<>(roomBean, HttpStatus.CREATED);
    }

    /**
     * Endpoint to retrieve all rooms.
     *
     * @return ResponseEntity with the list of all rooms.
     */
    @GetMapping(path = "/getAll")
    public ResponseEntity<List<RoomBean>> getAllRooms() {
        logger.info("Retrieving all rooms ");
        List<RoomBean> list = roomService.getAll();
        logger.info("Retrieved all rooms successfully");
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /**
     * Endpoint to retrieve rooms by ward ID.
     *
     * @param id The ID of the ward.
     * @return ResponseEntity with the list of rooms belonging to the specified ward.
     */
    @GetMapping(path = "/getByWardId/{id}")
    public ResponseEntity<List<RoomEntity>> getAllByWard(@PathVariable long id) {
        logger.info("Retrieving room by ward ID ");
        List<RoomEntity> list = roomService.findByWardId(id);
        logger.info("Retrieved rooms by ward ID successfully");
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /**
     * Endpoint to retrieve a room by its ID.
     *
     * @param id The ID of the room to retrieve.
     * @return ResponseEntity with the retrieved room details.
     */
    @GetMapping(path = "/getByRoomId/{id}")
    public ResponseEntity<RoomBean> getRoomById(@PathVariable Long id) {
        logger.info("Retrieving room by ID");
        RoomBean roombyid = roomService.getById(id);
        logger.info("Retrieved room by ID successfully");
        return new ResponseEntity<>(roombyid, HttpStatus.OK);
    }

    /**
     * Endpoint to delete a room by its ID.
     *
     * @param id The ID of the room to delete.
     * @return ResponseEntity indicating the success of the delete operation.
     */
    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<String> deleteRoomById(@PathVariable Long id) {
        roomService.delete(id);
        logger.info("Deleted room successfully");
        return new ResponseEntity<>("Deleted Successfully", HttpStatus.OK);
    }

    /**
     * Endpoint to update room details.
     *
     * @param roomBean The updated room details.
     * @return ResponseEntity indicating the success of the update operation.
     */
    @PutMapping(path = "/update")
    public ResponseEntity<Object> updateRoom(@RequestBody RoomBean roomBean) {
        roomService.update(roomBean.getId());
        logger.info("Room updated successfully");
        return ResponseEntity.ok().body("{\"message\": \"Room updated successfully\"}");
    }

    /**
     * Endpoint to update the status of a room.
     *
     * @param roomEntity The room entity whose status is to be updated.
     */
    @PutMapping("/updateStatus")
    public void updateStatus(@RequestBody RoomEntity roomEntity) {
        logger.info("Update the room status");
        roomService.updateStatus(roomEntity);
        logger.info(" Update the room status successfully");
    }

}
