package com.admin.service.implementation;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.admin.bean.BedAllocationBean;
import com.admin.bean.BedBean;
import com.admin.bean.PatientBean;
import com.admin.bean.RoomBean;
import com.admin.bean.WardBean;
import com.admin.constants.CommonConstants;
import com.admin.dto.BedAllocationDto;
import com.admin.entity.BedAllocation;
import com.admin.entity.BedEntity;
import com.admin.entity.RoomEntity;
import com.admin.entity.Ward;
import com.admin.exception.ExternalServiceException;
import com.admin.exception.PatientDoesNotExistsException;
import com.admin.exception.RecordNotFoundException;
import com.admin.exception.RoomAvailabilityException;
import com.admin.exception.WardAvailabilityException;
import com.admin.repository.BedAllocationRepository;
import com.admin.repository.BedEntityRepository;
import com.admin.repository.RoomRepository;
import com.admin.repository.WardRepository;
import com.admin.service.BedAllocationService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class BedAllocationServiceImpl implements BedAllocationService {

	@Autowired

	BedAllocationRepository bedAllocationRepository;
	@Autowired
	WardRepository wardRepository;
	@Autowired
	RoomRepository roomRepository;
	@Autowired
	BedEntityRepository bedRepository;

	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	BedEntityRepository bedEntityRepository;

	private static Logger log = LoggerFactory.getLogger(BedAllocationServiceImpl.class.getSimpleName());
	@Autowired
	private ObjectMapper objectMapper;
    /**
     * Retrieves patient details by ID.
     *
     * @param id The ID of the patient.
     * @return Patient details.
     * @throws ExternalServiceException If there is an error communicating with the registration service.
     */

	@Override
	public PatientBean getDetails(int id) {
		try {
			log.info("Retriving patient details by id");
			String url = "http://localhost:8103/patient-service/patients/" + id;

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> httpEntity = new HttpEntity<>(headers);

			ResponseEntity<PatientBean> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity,
					PatientBean.class);

			PatientBean patientBean = responseEntity.getBody();

			if (patientBean != null) {
				return patientBean;
			} else {
				throw new PatientDoesNotExistsException("Patient does not exists with given id ");
			}

		} catch (RestClientException exception) {
            throw new ExternalServiceException("Error communicating with registration service");
        }

	}
    /**
     * Saves bed allocation details.
     *
     * @param bedAllocationBean The bed allocation details to be saved.
     * @return Saved bed allocation details.
     */


	public BedAllocationBean save(BedAllocationBean bedAllocationBean) {

		try {
			log.info("Saving bed allocation details");

			BedAllocation bedallocate = bedAllocationRepository
					.getByPatientNumber(bedAllocationBean.getPatientNumber());
			if (bedallocate == null) {
				WardBean wardBean = bedAllocationBean.getBedId().getRoomId().getWardId();
				RoomBean roomBean = bedAllocationBean.getBedId().getRoomId();
				BedBean bedBean = bedAllocationBean.getBedId();
				bedBean.setStatus(CommonConstants.BOOKED);

				BedEntity bedEntity = new BedEntity();
				bedEntity = objectMapper.convertValue(bedBean, BedEntity.class);
				bedEntityRepository.save(bedEntity);
				if (roomBean.getAvailability() > 0) {
					roomBean.setAvailability(roomBean.getAvailability() - 1);
					RoomEntity roomEntity = new RoomEntity();
					roomEntity = objectMapper.convertValue(roomBean, RoomEntity.class);
					roomRepository.save(roomEntity);
					if (wardBean.getAvailability() > 0) {
						wardBean.setAvailability(wardBean.getAvailability() - 1);
						bedAllocationBean.setStatus(CommonConstants.ACTIVE);
						long days = ChronoUnit.DAYS.between(bedAllocationBean.getStartDate().toLocalDate(),
								bedAllocationBean.getEndDate().toLocalDate());
						bedAllocationBean.setNoOfDays(days);
						BedAllocation bedAllocation = objectMapper.convertValue(bedAllocationBean, BedAllocation.class);
						bedAllocationRepository.save(bedAllocation);
						Ward wardEntity = new Ward();
						wardEntity = objectMapper.convertValue(wardBean, Ward.class);
						wardRepository.save(wardEntity);
					} else {
						throw new WardAvailabilityException("No availability in the ward");
					}
				} else {
					throw new RoomAvailabilityException("No availability in the Room");
				}

			}
			return bedAllocationBean;
		} catch (Exception exception) {
			log.error("Error Occured while saving bed allocation details");
			throw exception;
		}
	}
    /**
     * Retrieves bed allocation details by ID.
     *
     * @param id The ID of the bed allocation.
     * @return Bed allocation details.
     * @throws RecordNotFoundException If no record is found with the given ID.
     */

	@Override
	public BedAllocationBean getById(int id) {

		try {
			log.info("Retrieving bed allocation by id");
			BedAllocation bedAllocation = bedAllocationRepository.findById(id)
					.orElseThrow(() -> new RecordNotFoundException("No Record Found with given id"));
			return objectMapper.convertValue(bedAllocation, BedAllocationBean.class);

		} catch (Exception exception) {
			log.error("Error Occured while retrieving bed allocation details by id ");
			throw exception;
		}
	}
    /**
     * Retrieves all bed allocation details.
     *
     * @return List of all bed allocation details.
     */


	@Override
	public List<BedAllocationBean> getAll() {

		try {
			log.info("Retrieving all bed allocation details");
			List<BedAllocation> entityList = bedAllocationRepository.findAll();
			List<BedAllocationBean> beanList = new ArrayList<>();
			entityToBean(entityList, beanList);
			return beanList;
		} catch (Exception exception) {
			log.error("Error Occured while retrieving all bed allocation details ");
			throw exception;
		}
	}

	private void entityToBean(List<BedAllocation> entitylist, List<BedAllocationBean> beanList) {

		for (BedAllocation bedAllocation : entitylist) {
			BedAllocationBean bedAllocationBean = new BedAllocationBean();
			bedAllocationBean = objectMapper.convertValue(bedAllocation, BedAllocationBean.class);
			beanList.add(bedAllocationBean);
		}
	}
	/**
     * Deletes a bed allocation by ID.
     *
     * @param id The ID of the bed allocation to delete.
     */

	@Override
	public void delete(int id) {

		try {
			log.error("Deleting bed allocation by id");
			bedAllocationRepository.deleteById(id);
		} catch (Exception exception) {
			log.error("Error Occured while deleting bed allocation details by id ");
			throw exception;
		}

	}
	/**
	 * Updates the bed allocation details.
	 *
	 * @param bedAllocationBean The bed allocation details to be updated.
	 * @throws RecordNotFoundException If no record is found with the given ID.
	 */

	@Override
	public void update(BedAllocationBean bedAllocationBean) {

		try {
			log.info("updating bed allocation details");
			BedAllocation bedAllocation = bedAllocationRepository.getReferenceById(bedAllocationBean.getId());
			bedAllocation.setId(bedAllocationBean.getId());
			bedAllocation.setStartDate(bedAllocationBean.getStartDate());
			bedAllocation.setEndDate(bedAllocationBean.getEndDate());
			bedAllocation.setNoOfDays(bedAllocationBean.getNoOfDays());
			bedAllocation.setPatientId(bedAllocationBean.getPatientId());
			BedBean bedBean = bedAllocationBean.getBedId();
			BedEntity bedEntity = new BedEntity();
			bedEntity = objectMapper.convertValue(bedBean, BedEntity.class);
			bedAllocation.setBedId(bedEntity);
			bedAllocation.setStatus(bedAllocationBean.getStatus());
			bedAllocationRepository.save(bedAllocation);
		} catch (Exception exception) {
			log.error("Error Occured while updating bed allocation details by id ");
			throw exception;
		}
	}

	@Override
	public List<BedAllocationDto> getBedDetails() {

		List<BedAllocationDto> bedDetails = bedAllocationRepository.getBedAllocationDetails();

		return bedDetails;
	}

	@Scheduled(fixedRate = 60000) // 5 minutes in milliseconds
	public void getDetails() {
		try {
			System.out.println("scheduled start");
			List<BedAllocation> bedDetails = bedAllocationRepository.findBedAllocationsWithEndDateBeforeCurrentDate();
			for (BedAllocation list : bedDetails) {
				BedEntity entity = list.getBedId();
				if (entity.getStatus().equalsIgnoreCase(CommonConstants.BOOKED)) {
					entity.setStatus(CommonConstants.EMPTY);
					bedRepository.save(entity);
					RoomEntity room = entity.getRoomId();
					room.setAvailability(room.getAvailability() + 1);
					roomRepository.save(room);

					Ward ward = entity.getRoomId().getWardId();
					ward.setAvailability(ward.getAvailability() + 1);
					wardRepository.save(ward);
				}

			}
			System.out.println("scheduled end");
		} catch (Exception e) {

			log.error("Error cleaning up expired OTPs: " + e.getMessage(), e);
		}
	}
	/**
	 * Retrieves all bed allocation details with additional information such as patient names.
	 *
	 * @return List of maps containing bed allocation details along with patient names.
	 * @throws ExternalServiceException If there is an error communicating with the registration service.
	 */

	public List<Map<String, Object>> getAllBedAllocationsWithPatientNames() {
		try {
			log.info("Retrieving all bed allocations with patient names");
			List<BedAllocation> bedAllocations = bedAllocationRepository.findAll();
			List<Map<String, Object>> mappedData = new ArrayList<>();

			for (BedAllocation bedAllocation : bedAllocations) {
				Map<String, Object> allocationMap = new HashMap<>();
				allocationMap.put("id", bedAllocation.getId());
				allocationMap.put("noOfDays", bedAllocation.getNoOfDays());
				allocationMap.put("startDate", bedAllocation.getStartDate());
				allocationMap.put("endDate", bedAllocation.getEndDate());
				allocationMap.put("status", bedAllocation.getStatus());
				allocationMap.put("bedId", bedAllocation.getBedId());
				// Fetch PatientRegistration and map patientId to patientName
				PatientBean patient = getDetails(bedAllocation.getPatientId());
				if (patient != null) {
					String patientName = patient.getFirstName() + " " + patient.getLastName();
					allocationMap.put("patientName", patientName);
				} else {
					allocationMap.put("patientName", "Unknown");
				}

				mappedData.add(allocationMap);
			}

			return mappedData;
		} catch (Exception exception) {
			log.error("Error Occured while retrieving all bed allocations with patient names");
			throw exception;
		}
	}
	/**
	 * Retrieves bed allocation details for updating by patient number.
	 *
	 * @param patientNo The patient number for which bed allocation details need to be updated.
	 * @return Bed allocation details.
	 * @throws RecordNotFoundException If no record is found for the given patient number.
	 */

	public BedAllocation getDetailsForUpdating(String patientNo) {

		try {
			log.info("Updating bed allocation details by patient number");
			return bedAllocationRepository.getDetailsForUpdating(patientNo);
		} catch (Exception exception) {
			log.error("Error Occured while updating bed allocation details by patient number");
			throw exception;
		}
	}
}
