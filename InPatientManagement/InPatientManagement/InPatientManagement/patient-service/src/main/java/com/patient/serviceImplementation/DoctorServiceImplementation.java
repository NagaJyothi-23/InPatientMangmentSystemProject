/**
 * Service implementation class for managing doctor-related operations.
 */
package com.patient.serviceImplementation;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.patient.bean.DoctorBean;
import com.patient.constants.CommonConstants;
import com.patient.entity.DoctorEntity;
import com.patient.exception.DoctorDetailsNotFoundException;
import com.patient.exception.DoctorIdNotFoundException;
import com.patient.repository.DoctorRepository;
import com.patient.service.DoctorService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class DoctorServiceImplementation implements DoctorService {

    @Autowired
    DoctorRepository doctorRepository;
    @PersistenceContext
    private EntityManager entityManager;
    ObjectMapper objectMapper = new ObjectMapper();
    private static Logger log = LoggerFactory.getLogger(DoctorServiceImplementation.class.getSimpleName());

    /**
     * Saves doctor details.
     * @param doctorBean The doctor details to be saved.
     * @return The saved doctor details.
     */
    @Override
    public DoctorBean saveDoctorDetails(DoctorBean doctorBean) {
        doctorBean.setStatus("Active");
        DoctorEntity doctorEntity = objectMapper.convertValue(doctorBean, DoctorEntity.class);
        doctorRepository.save(doctorEntity);
        return doctorBean;
    }

    /**
     * Retrieves doctor details by ID.
     * @param id The ID of the doctor.
     * @return The doctor details.
     * @throws DoctorIdNotFoundException If doctor with the given ID is not found.
     */
    @Override
    public DoctorBean getDoctorById(long id) {
        log.info("Get by Doctor Id");
        DoctorEntity doctorEntity = doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorIdNotFoundException("Record not found with given id"));
        DoctorBean doctorBean = objectMapper.convertValue(doctorEntity, DoctorBean.class);
        return doctorBean;
    }

    /**
     * Retrieves all doctor details.
     * @return List of doctor details.
     * @throws DoctorDetailsNotFoundException If doctor details are not found.
     */
    @Override
    public List<DoctorBean> getAllDoctorDetails() {
        log.info("Getting all Doctor details");
        try {
            List<DoctorBean> doctorBean = new ArrayList<>();
            List<DoctorEntity> doctorEntity = doctorRepository.findAll();
            if (doctorEntity != null) {
                entityToBean(doctorEntity, doctorBean);
                log.info("Getting all Doctor details");
                return doctorBean;
            } else {
                throw new DoctorDetailsNotFoundException("Doctor details not found");
            }
        } catch (Exception e) {
            log.error("An error occurred while getting all doctors: " + e.getMessage());
            throw e;
        }
    }

    private void entityToBean(List<DoctorEntity> doctorEntity, List<DoctorBean> doctorBean) {
        for (DoctorEntity doctorEntity1 : doctorEntity) {
            DoctorBean doctorBean1 = objectMapper.convertValue(doctorEntity1, DoctorBean.class);
            doctorBean.add(doctorBean1);
        }
    }

    /**
     * Deletes a doctor by ID.
     * @param id The ID of the doctor to delete.
     */
    @Override
    public void delete(Long id) {
        log.info("Deleting Doctor ");
        try {
            doctorRepository.deleteById(id);
            log.info("deleting Doctor Bean completed");
        } catch (Exception e) {
            log.error("An error occurred while deleting doctor: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Updates doctor details.
     * @param doctorbean The doctor details to be updated.
     * @throws DoctorDetailsNotFoundException If doctor details are not found.
     */
    @Override
    public void updateDoctorDetails(DoctorBean doctorbean) {
        if (doctorbean != null) {
            log.info("Updating doctor Bean");
            DoctorEntity doctorEntity = doctorRepository.getReferenceById(doctorbean.getId());
            DoctorBean doctorBean = new DoctorBean();
            doctorEntity.setId(doctorBean.getId());
            doctorEntity.setName(doctorBean.getName());
            doctorEntity.setDepartmentId(doctorBean.getDepartmentId());
            doctorRepository.save(doctorEntity);
            log.info("Updating Doctor Bean completed");
        } else {
            throw new DoctorDetailsNotFoundException("Doctor details not found ");
        }
    }

    /**
     * Updates the status of a doctor.
     * @param doctor The doctor entity whose status to update.
     */
    @Override
    public void updateStatus(DoctorEntity doctor) {
        if (doctor.getStatus().equalsIgnoreCase(CommonConstants.Active)) {
            doctor.setStatus(CommonConstants.InActive);
        } else {
            doctor.setStatus(CommonConstants.Active);
        }
        doctorRepository.save(doctor);
    }

    /**
     * Retrieves all doctor details with department names.
     * @return List of objects containing doctor details along with department names.
     */
    @Override
    public List<Object[]> getAllWithDept() {
        return doctorRepository.getAllWithDept();
    }

}
