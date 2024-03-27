package com.admin.service.implementation;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.admin.bean.RoomBean;
import com.admin.bean.WardBean;
import com.admin.constants.CommonConstants;
import com.admin.entity.RoomEntity;
import com.admin.exception.RecordNotFoundException;
import com.admin.exception.RoomAlreadyExistsException;
import com.admin.exception.WardCapacityExceededException;
import com.admin.repository.RoomRepository;
import com.admin.service.RoomService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Implementation of the RoomService interface that handles business logic
 * related to rooms.
 */
@Service
public class RoomServiceImplementation implements RoomService {

	@Autowired
	RoomRepository roomRepository;
	ObjectMapper objectMapper = new ObjectMapper();
	Logger log = LoggerFactory.getLogger(RoomServiceImplementation.class);

	/**
	 * Saves a new room.
	 * 
	 * @param roomBean The room details to be saved.
	 * @throws RoomAlreadyExistsException    If the room with the same number
	 *                                       already exists.
	 * @throws WardCapacityExceededException If adding this room would exceed the
	 *                                       capacity of the ward.
	 */
	@Override
	public void savingRoom(RoomBean roomBean) {
		log.info("Saving room");
		RoomEntity roomEntity1 = roomRepository.getByRoomNoAndWardId_Id(roomBean.getRoomNo(),
				roomBean.getWardId().getId());
		if (roomEntity1 == null) {
			roomBean.setStatus(CommonConstants.ACTIVE);
			roomBean.setAvailability(roomBean.getRoomSharing());
			WardBean ward = roomBean.getWardId();
			Integer totalRoomSharing = roomRepository.sumRoomSharingByWard(ward.getId());
			if (totalRoomSharing == null) {
				totalRoomSharing = 0;
			}
			if (totalRoomSharing + roomBean.getRoomSharing() <= ward.getCapacity()) {
				RoomEntity roomEntity = objectMapper.convertValue(roomBean, RoomEntity.class);
				roomRepository.save(roomEntity);
				log.info("Room saved successfully");
			} else {
				throw new WardCapacityExceededException("Room capacity exceeded for ward");
			}
		} else {
			throw new RoomAlreadyExistsException("That room already exists");
		}
	}

	/**
	 * Retrieves all rooms.
	 * 
	 * @return List of RoomBean objects representing all rooms.
	 */
	@Override
	public List<RoomBean> getAll() {
		try {
			log.info("Fetching all rooms");
			List<RoomBean> roomBean = new ArrayList<>();
			List<RoomEntity> roomEntity = roomRepository.findAll();
			entityToBean(roomEntity, roomBean);
			return roomBean;
		} catch (Exception exception) {
			log.info("Error occurred while fetching all rooms", exception);
			throw exception;
		}
	}

	/**
	 * Retrieves a room by its ID.
	 * 
	 * @param id The ID of the room to retrieve.
	 * @return The RoomBean object representing the room.
	 * @throws RecordNotFoundException If the room with the given ID is not found.
	 */
	@Override
	public RoomBean getById(long id) {
		log.info("Fetching room by id");
		RoomEntity roomEntity = roomRepository.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("record not found"));
		return objectMapper.convertValue(roomEntity, RoomBean.class);
	}

	/**
	 * Retrieves a room for update by its ID.
	 * 
	 * @param id The ID of the room to retrieve for update.
	 * @return The RoomBean object representing the room.
	 * @throws RecordNotFoundException If the room with the given ID is not found.
	 */
	@Override
	public RoomBean update(long id) {
		log.info("updating room");
		RoomEntity roomEntity = roomRepository.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("record not found"));
		return objectMapper.convertValue(roomEntity, RoomBean.class);
	}

	/**
	 * Deletes a room by its ID.
	 * 
	 * @param id The ID of the room to delete.
	 */
	@Override
	public void delete(long id) {
		try {
			log.info("Deleting room");
			roomRepository.deleteById(id);
		} catch (Exception exception) {
			log.info("Error occurred while deleting room", exception);
			throw exception;
		}
	}

	/**
	 * Converts a list of RoomEntity objects to a list of RoomBean objects.
	 * 
	 * @param listEntity The list of RoomEntity objects to convert.
	 * @param listbean   The list of RoomBean objects to populate.
	 */
	public void entityToBean(List<RoomEntity> listEntity, List<RoomBean> listbean) {
		for (RoomEntity roomEntity : listEntity) {
			RoomBean roomBean = new RoomBean();
			roomBean = objectMapper.convertValue(roomEntity, RoomBean.class);
			listbean.add(roomBean);
		}
	}

	/**
	 * Retrieves rooms by ward ID.
	 * 
	 * @param wardId The ID of the ward to retrieve rooms for.
	 * @return List of RoomEntity objects representing rooms in the ward.
	 */
	@Override
	public List<RoomEntity> findByWardId(Long wardId) {
		try {
			log.info("fetching rooms by wardId");
			return roomRepository.findByWardId_Id(wardId);
		} catch (Exception exception) {
			log.info("Error occurred while fetching rooms by wardId", exception);
			throw exception;
		}
	}

	/**
	 * Updates the status of a room (active or inactive).
	 * 
	 * @param roomEntity The room entity to update.
	 */
	@Override
	public void updateStatus(RoomEntity roomEntity) {
		if (roomEntity.getStatus().equalsIgnoreCase(CommonConstants.ACTIVE)) {
			roomEntity.setStatus(CommonConstants.INACTIVE);
		} else {
			roomEntity.setStatus(CommonConstants.ACTIVE);
		}
		roomRepository.save(roomEntity);
	}
}
