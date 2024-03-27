/**
 * This class implements the {@link RoomTypeService} interface and provides
 * methods for managing room types.
 */
package com.admin.service.implementation;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.admin.bean.RoomTypeBean;
import com.admin.constants.CommonConstants;
import com.admin.entity.RoomType;
import com.admin.exception.RecordNotFoundException;
import com.admin.exception.RoomTypeAlreadyExistsException;
import com.admin.exception.RoomTypeDetailsNotFoundException;
import com.admin.repository.RoomTypeRepository;
import com.admin.service.RoomTypeService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RoomTypeServiceImpl implements RoomTypeService {

    @Autowired
    RoomTypeRepository roomTypeRepository;
    @Autowired
    private ObjectMapper objectMapper;
    Logger log = LoggerFactory.getLogger(RoomTypeServiceImpl.class);

    /**
     * Saves a new room type if it does not already exist.
     *
     * @param roomTypeBean The room type details to be saved.
     * @return The saved room type bean.
     * @throws RoomTypeAlreadyExistsException if the room type already exists.
     */
    @Override
    public RoomTypeBean save(RoomTypeBean roomTypeBean) {

        log.info("Saving room type");
        RoomType roomtype1 = roomTypeRepository.getByName(roomTypeBean.getName());
        if (roomtype1 == null) {
            RoomType roomType = objectMapper.convertValue(roomTypeBean, RoomType.class);
            roomType.setStatus(CommonConstants.ACTIVE);
            roomTypeRepository.save(roomType);
        } else {
            log.info("Room type already exists");
            throw new RoomTypeAlreadyExistsException("Room type already exists");
        }
        return roomTypeBean;

    }

    /**
     * Retrieves all room types.
     *
     * @return List of room type beans.
     * @throws Exception if an error occurs while fetching room types.
     */
    @Override
    public List<RoomTypeBean> getAll() {

        try {
            log.info("Fetching all room types");
            List<RoomType> entityList = roomTypeRepository.findAll();
            List<RoomTypeBean> beanList = new ArrayList<>();
            entityListToBeanList(entityList, beanList);
            return beanList;
        } catch (Exception exception) {
            log.error("Error occurred while fetching all room types", exception);
            throw exception;
        }
    }

    /**
     * Converts a list of room type entities to a list of room type beans.
     *
     * @param entityList List of room type entities.
     * @param beanList   List of room type beans.
     */
    private void entityListToBeanList(List<RoomType> entityList, List<RoomTypeBean> beanList) {

        for (RoomType roomType : entityList) {
            RoomTypeBean roomTypeBean = objectMapper.convertValue(roomType, RoomTypeBean.class);
            beanList.add(roomTypeBean);
        }
    }

    /**
     * Retrieves a room type by its ID.
     *
     * @param id The ID of the room type to retrieve.
     * @return The room type bean corresponding to the given ID.
     * @throws RecordNotFoundException if no room type is found with the given ID.
     */
    @Override
    public RoomTypeBean getById(long id) {

        log.info("Fetching room type by ID");
        RoomType roomType = roomTypeRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("No record found with the given ID"));
        RoomTypeBean roomTypeBean = objectMapper.convertValue(roomType, RoomTypeBean.class);
        log.info("Fetching room type by ID is done");
        return roomTypeBean;

    }

    /**
     * Deletes a room type by its ID.
     *
     * @param id The ID of the room type to delete.
     * @throws Exception if an error occurs while deleting the room type.
     */
    @Override
    public void delete(long id) {

        try {
            log.info("Deleting the room type");
            roomTypeRepository.deleteById(id);
        } catch (Exception exception) {
            log.error("Error occurred while deleting room type by ID", exception);
            throw exception;
        }
    }

    /**
     * Updates the details of a room type.
     *
     * @param roomTypeBean The updated room type details.
     * @return The updated room type entity.
     * @throws RoomTypeDetailsNotFoundException if room type details are not found.
     */
    @Override
    public RoomType updateRoomType(RoomTypeBean roomTypeBean) {
        if (roomTypeBean != null) {
            log.info("Updating the room type by ID");
            RoomType roomType = roomTypeRepository.getReferenceById(roomTypeBean.getId());
            roomType = objectMapper.convertValue(roomTypeBean, RoomType.class);
            roomTypeRepository.save(roomType);
            log.info("Updating the room type by ID is done");
            return roomType;
        } else {
            log.error("Error occurred while updating the room type by ID");
            throw new RoomTypeDetailsNotFoundException("Room type details not found");
        }
    }

    /**
     * Updates the status of a room type (Active/Inactive).
     *
     * @param roomTypeEntity The room type entity whose status is to be updated.
     * @throws RoomTypeDetailsNotFoundException if room type details are not found.
     */
    @Override
    public void updateStatus(RoomType roomTypeEntity) {
        if (roomTypeEntity != null) {
            log.info("Updating the status of room type");
            if (roomTypeEntity.getStatus().equalsIgnoreCase(CommonConstants.ACTIVE)) {
                roomTypeEntity.setStatus(CommonConstants.INACTIVE);
            } else {
                roomTypeEntity.setStatus(CommonConstants.ACTIVE);
            }
            roomTypeRepository.save(roomTypeEntity);
        } else {
            log.error("Error occurred while updating the status of room type");
            throw new RoomTypeDetailsNotFoundException("Room type details not found");
        }

    }

}
