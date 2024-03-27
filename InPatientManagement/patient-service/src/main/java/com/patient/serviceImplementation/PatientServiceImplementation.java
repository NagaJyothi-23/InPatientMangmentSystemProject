/**
 * Service implementation class for managing patient-related operations.
 */
package com.patient.serviceImplementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.patient.bean.PatientBean;
import com.patient.entity.PatientEntity;
import com.patient.exception.DoctorNameNotFoundException;
import com.patient.exception.PatientDetailsNotFoundException;
import com.patient.exception.PatientIdNotFoundException;
import com.patient.repository.PatientRepository;
import com.patient.service.PatientService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class PatientServiceImplementation implements PatientService {
    @Autowired
    private PatientRepository patientRepository;
    ObjectMapper objectMapper = new ObjectMapper();
    @PersistenceContext
    private EntityManager entityManager;
    private int currentYear;
    private int currentMonth;
    private int currentNumber;
    private static Logger log = LoggerFactory.getLogger(PatientServiceImplementation.class.getSimpleName());

    /**
     * Saves patient details.
     * 
     * @param patientBean The patient details to be saved.
     * @return The saved patient details.
     */
    @Override
    public PatientBean savePatientDetails(PatientBean patientBean) {
        log.info("Saving the patient details");
        try {
            PatientEntity patientEntity = objectMapper.convertValue(patientBean, PatientEntity.class);

            String patientNumber = generatePatientNo();
            patientEntity.setPatientNumber(patientNumber);
            patientRepository.save(patientEntity);
            log.info("Patient details saved successfully");
            return patientBean;
        } catch (Exception e) {
            log.error("Error occurred while saving patient details: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Retrieves all patient details.
     * 
     * @return List of patient details.
     * @throws PatientDetailsNotFoundException If patient details are not found.
     */
    @Override
    public List<PatientBean> getAllPatientDetails() throws PatientDetailsNotFoundException {
        log.info("Getting all patient details");
        List<PatientBean> patientBeanList = new ArrayList<>();
        List<PatientEntity> patientEntityList = patientRepository.findAll();
        if (patientEntityList != null) {
            entityToBean(patientEntityList, patientBeanList);
            log.info("Retrieved all patient details successfully");
            return patientBeanList;
        } else {
            log.info("patient details not found");
            throw new PatientDetailsNotFoundException("patient details not found");
        }
    }

    /**
     * Retrieves patient details by ID.
     * 
     * @param id The ID of the patient.
     * @return The patient details.
     * @throws PatientIdNotFoundException If patient with the given ID is not found.
     */
    @Override
    public Optional<PatientEntity> getPatientById(Integer id) throws PatientIdNotFoundException {
        log.info("Getting patient details by ID");
        Optional<PatientEntity> patientEntityOptional = patientRepository.findById(id);
        if (!patientEntityOptional.isPresent()) {
            log.error("Patient with ID " + id + " not found");
            throw new PatientIdNotFoundException("Patient with ID " + id + " not found");
        } else {
            log.info("Retrieved patient details by ID successfully");
            return patientEntityOptional;
        }
    }

    /**
     * Converts a list of PatientEntity objects to a list of PatientBean objects.
     * 
     * @param patientEntityList List of PatientEntity objects.
     * @param patientBeanList   List to store converted PatientBean objects.
     */
    private void entityToBean(List<PatientEntity> patientEntityList, List<PatientBean> patientBeanList) {
        for (PatientEntity patientEntity : patientEntityList) {
            PatientBean patientBean = objectMapper.convertValue(patientEntity, PatientBean.class);
            patientBeanList.add(patientBean);
        }
    }

    /**
     * Retrieves patient details by doctor name.
     * 
     * @param doctorName The name of the doctor.
     * @return List of patient details.
     * @throws DoctorNameNotFoundException If doctor name is not found.
     */
    public List<Object[]> getPatientDetailsByDoctor(String doctorName) throws DoctorNameNotFoundException {
        if (doctorName != null) {
            log.info("Getting patient details by doctor name");
            return patientRepository.findPatientDetailsByDoctorName(doctorName);
        } else {
            log.info("doctor name not found");
            throw new DoctorNameNotFoundException("doctor name not found");
        }
    }

    /**
     * Retrieves patient details by full name.
     * 
     * @param fullName The full name of the patient.
     * @return List of patient details.
     */
    public List<Object[]> getPatientDetailsByFullName(String fullName) {
        log.info("Getting patient details by full name");
        return patientRepository.getPatientDetailsByFullName(fullName);
    }

    /**
     * Updates the status of a patient to "Inactive".
     * 
     * @param patient The patient entity to update.
     */
    @Override
    public void updateStatus(PatientEntity patient) {
        log.info("Updating the patient status to Inactive");
        patient.setStatus("Inactive");
        patientRepository.save(patient);
    }

    /**
     * Generates a patient number.
     * 
     * @return The generated patient number.
     */
    @Override
    public String generatePatientNo() {
        log.info("Generating patient number");
        int year = java.time.Year.now().getValue();
        int month = java.time.MonthDay.now().getMonthValue();

        if (year != currentYear || month != currentMonth) {
            currentYear = year;
            currentMonth = month;
            currentNumber = 1;
        } else {
            currentNumber++;
        }

        String patientNumber = String.format("IN-%02d-%02d-%04d", currentYear % 100, currentMonth, currentNumber);
        log.info("Patient number generated successfully: " + patientNumber);
        return patientNumber;
    }
}
