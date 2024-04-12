/**
 * Service implementation class for handling patient billing operations.
 */
package com.patient.billing.service.serviceimplimentation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.patient.billing.service.bean.PatientBillingBean;
import com.patient.billing.service.constants.CommonConstants;
import com.patient.billing.service.controller.PatientBillingController;
import com.patient.billing.service.dto.BedAllocationDto;
import com.patient.billing.service.dto.PatientBillingDTO;
import com.patient.billing.service.entity.BedEntity;
import com.patient.billing.service.entity.PatientBillingEntity;
import com.patient.billing.service.entity.RoomEntity;
import com.patient.billing.service.exception.BedAllocationNotFoundException;
import com.patient.billing.service.exception.BillingDetailsNotFoundException;
import com.patient.billing.service.exception.PatientNumberNotFoundException;
import com.patient.billing.service.repository.PatientBillingRepository;
import com.patient.billing.service.service.PatientBillingService;

@Service
public class PatientBillingServiceImplimentation implements PatientBillingService {
    @Autowired
    private PatientBillingRepository patientBillingRepository;

    private static Logger log = LoggerFactory.getLogger(PatientBillingController.class.getSimpleName());

    /**
     * Saves billing details.
     * @param billing The billing details to be saved.
     */
    @Override
    public void savebillingDetails(BedAllocationDto billing) {
        PatientBillingEntity patientBillingEntity = new PatientBillingEntity();
        log.info("saving the billing details");
        beanToEntity(patientBillingEntity, billing);

        patientBillingRepository.save(patientBillingEntity);
        log.info("billing details saved successfully");
    }

    private void beanToEntity(PatientBillingEntity patientBillingEntity, BedAllocationDto bedAllocation) {
        patientBillingEntity.setBillingDate(LocalDate.now());
        patientBillingEntity.setBedAllocationId(bedAllocation.getId());

        long days = bedAllocation.getNoOfDays();
        BedEntity BedBean = bedAllocation.getBedId();
        RoomEntity room = BedBean.getRoomId();
        double roomPrice = room.getRoomPrice();

        double totalAmount = roomPrice * days;
        patientBillingEntity.setRecordStatus(CommonConstants.ACTIVE);
        patientBillingEntity.setPaidAmount(CommonConstants.PAIDAMOUNT);
        patientBillingEntity.setTotalAmount(totalAmount);
        double remainingAmount = totalAmount - CommonConstants.PAIDAMOUNT;
        patientBillingEntity.setRemainingAmount(remainingAmount);
        if (remainingAmount + CommonConstants.PAIDAMOUNT == totalAmount) {
            patientBillingEntity.setPaymentStatus(CommonConstants.COMPLETED);
        } else {
            patientBillingEntity.setPaymentStatus(CommonConstants.PENDING);
        }
    }

    /**
     * Retrieves all billing details.
     * @return Optional list of billing details, empty if not found.
     * @throws BillingDetailsNotFoundException If billing details are not found.
     */
    @Override
    public Optional<List<PatientBillingDTO>> getAllBillingDetails() {
        Optional<List<PatientBillingDTO>> billingDetails = patientBillingRepository.getBillingDetails();
        log.info("getting the billing details");
        if (billingDetails.isPresent()) {
            log.info("getting the billing details successfully");
            return billingDetails;
        } else {
            log.info("billing details not found");
            throw new BillingDetailsNotFoundException();
        }
    }

    /**
     * Filters billing details by date range.
     * @param startDate Start date of the range.
     * @param endDate End date of the range.
     * @return Optional list of filtered billing details, empty if not found.
     * @throws BillingDetailsNotFoundException If billing details are not found for the specified date range.
     */
    @Override
    public Optional<List<PatientBillingDTO>> filterBillingDetailsByDateRange(LocalDate startDate, LocalDate endDate) {
        Optional<List<PatientBillingDTO>> billingDetails = patientBillingRepository
                .getBillingDetailsBetweenTheDates(startDate, endDate);
        log.info("getting the billing details based on start date and end date");
        if (billingDetails.isPresent()) {
            log.info("get billing details based on start date and end date successfully");
            return billingDetails;
        } else {
            log.info("billing details not found with these dates");
            throw new BillingDetailsNotFoundException("billing details are not found in this dates");
        }
    }

    /**
     * Updates the status of a billing entity to "InActive".
     * @param patientBillingEntity The billing entity to be updated.
     */
    @Override
    public void updateStatus(PatientBillingEntity patientBillingEntity) {
        log.info("updating the status of billing");
        patientBillingEntity.setStatus("InActive");
        patientBillingRepository.save(patientBillingEntity);
        log.info("updated the status successfully");
    }

    /**
     * Retrieves bed allocation details based on patient number.
     * @param patientNumber The patient number.
     * @return Optional bed allocation details, empty if not found.
     * @throws PatientNumberNotFoundException If patient number is not found.
     * @throws BedAllocationNotFoundException If bed allocation is not found for the specified patient number.
     */
    @Override
    public Optional<BedAllocationDto> getBedAllocationDetailsBasedOnPatientNumber(String patientNumber) {
        if (patientNumber != null) {
            log.info("getting the details by using patient number");
            Optional<BedAllocationDto> bedAllocationdetails = patientBillingRepository
                    .findPatientDetailsByPatientNumber(patientNumber);
            if (bedAllocationdetails.isPresent()) {
                log.info("get the details by using patient number is done");
                return bedAllocationdetails;
            } else {
                log.info("there is no details with patient number");
                throw new BedAllocationNotFoundException("Bed is not allocated for this patient number");
            }
        } else {
            log.info("patient number not found");
            throw new PatientNumberNotFoundException("Patient number is not found");
        }
    }
}
