package com.admin.service.implementation;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.admin.bean.BedBean;
import com.admin.bean.RoomBean;
import com.admin.constants.CommonConstants;
import com.admin.entity.BedEntity;
import com.admin.entity.RoomEntity;
import com.admin.exception.BedAlreadyExistsException;
import com.admin.exception.RecordNotFoundException;
import com.admin.exception.RoomCapacityExceededException;
import com.admin.repository.BedEntityRepository;
import com.admin.service.BedService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Implementation of the BedService interface.
 */
@Service
public class BedServiceImpl implements BedService {

    @Autowired
    BedEntityRepository bedEntityRepository;

    @Autowired
    private ObjectMapper objectMapper;
    private static Logger log = LoggerFactory.getLogger(BedServiceImpl.class.getSimpleName());

    /**
     * Saves a new bed.
     *
     * @param bedBean The bed details to be saved.
     * @return The saved bed details.
     * @throws RoomCapacityExceededException If the room bed capacity is exceeded.
     * @throws BedAlreadyExistsException     If a bed with the same bed number already exists in the room.
     */
    @Override
    public BedBean save(BedBean bedBean) {
        log.info("Saving bed");
        BedEntity bedEntity1 = bedEntityRepository.getByBedNoAndRoomId_Id(bedBean.getBedNo(),
                bedBean.getRoomId().getId());
        if (bedEntity1 == null) {
            RoomBean room = bedBean.getRoomId();
            Integer totalBeds = bedEntityRepository.sumBedsByRoom(room.getId());
            if (totalBeds == null) {
                totalBeds = 0;
            }
            if (totalBeds + 1 <= room.getRoomSharing()) {
                BedEntity bedEntity = objectMapper.convertValue(bedBean, BedEntity.class);
                bedBean.setStatus(CommonConstants.EMPTY);
                bedEntityRepository.save(bedEntity);
            } else {
                throw new RoomCapacityExceededException("Room bed capacity exceeded");
            }
        } else {
            throw new BedAlreadyExistsException("Already bed exists with this bedNo");
        }
        return bedBean;
    }

    /**
     * Retrieves a bed by its ID.
     *
     * @param bedId The ID of the bed to retrieve.
     * @return The bed details.
     * @throws RecordNotFoundException If no record is found with the given ID.
     */
    @Override
    public BedBean getById(long bedId) {
        log.info("Fetching bed by ID");
        BedEntity bedEntity = bedEntityRepository.findById(bedId)
                .orElseThrow(() -> new RecordNotFoundException("No Record Found with given ID"));
        BedBean bedBean = objectMapper.convertValue(bedEntity, BedBean.class);
        return bedBean;
    }

    /**
     * Retrieves all beds.
     *
     * @return List of all bed details.
     */
    @Override
    public List<BedBean> getAll() {
        try {
            log.info("Retrieving all beds");
            List<BedEntity> entityList = bedEntityRepository.findAll();
            List<BedBean> beanList = new ArrayList<>();
            entityToBean(entityList, beanList);
            return beanList;
        } catch (Exception exception) {
            log.error("Error occurred while retrieving beds", exception);
            throw exception;
        }
    }

    private void entityToBean(List<BedEntity> entityList, List<BedBean> beanList) {
        for (BedEntity bedEntity : entityList) {
            BedBean bedBean = objectMapper.convertValue(bedEntity, BedBean.class);
            beanList.add(bedBean);
        }
    }

    /**
     * Deletes a bed by its ID.
     *
     * @param id The ID of the bed to delete.
     */
    @Override
    public void delete(long id) {
        try {
            log.info("Deleting bed by ID");
            bedEntityRepository.deleteById(id);
        } catch (Exception exception) {
            log.error("Error occurred while deleting bed by ID", exception);
            throw exception;
        }
    }

    /**
     * Updates the details of a bed.
     *
     * @param bedId      The ID of the bed to update.
     * @param updatedBed The updated bed details.
     * @throws RecordNotFoundException If no record is found with the given ID.
     */
    @Override
    public void update(long bedId, BedBean updatedBed) {
        log.info("Updating bed");
        BedEntity bedEntity = bedEntityRepository.findById(bedId)
                .orElseThrow(() -> new RecordNotFoundException("No Record Found with given ID"));
        if (bedEntity != null) {
            bedEntity.setBedNo(updatedBed.getBedNo());
            RoomBean roomBean = updatedBed.getRoomId();
            RoomEntity roomEntity = new RoomEntity();
            roomEntity = objectMapper.convertValue(roomBean, RoomEntity.class);
            bedEntity.setRoomId(roomEntity);
            bedEntity.setStatus(updatedBed.getStatus());
            bedEntityRepository.save(bedEntity);
        } else {
            throw new RecordNotFoundException("Record not found");
        }
    }

    /**
     * Retrieves all beds associated with a specific room.
     *
     * @param roomEntityId The ID of the room.
     * @return List of bed details associated with the room.
     */
    @Override
    public List<BedBean> findByBedIdRoomEntityId(Long roomEntityId) {
        try {
            log.info("Retrieving beds by room ID");
            List<BedEntity> entityList = bedEntityRepository.findByRoomId_Id(roomEntityId);
            List<BedBean> beanList = new ArrayList<>();
            entityToBean(entityList, beanList);
            return beanList;
        } catch (Exception exception) {
            log.error("Error occurred while retrieving beds by room ID", exception);
            throw exception;
        }
    }
}
