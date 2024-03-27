/**
 * This class implements the {@link WardService} interface and provides
 * methods for managing wards.
 */
package com.admin.service.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.admin.bean.WardBean;
import com.admin.constants.CommonConstants;
import com.admin.entity.Department;
import com.admin.entity.Ward;
import com.admin.exception.DepartmentNotFoundException;
import com.admin.exception.RecordNotFoundException;
import com.admin.exception.WardAlreadyExistsException;
import com.admin.repository.WardRepository;
import com.admin.service.WardService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class WardServiceImpl implements WardService {

    @Autowired
    private WardRepository wardRepository;
    @Autowired
    private ObjectMapper objectMapper;
    private static Logger log = LoggerFactory.getLogger(BedAllocationServiceImpl.class.getSimpleName());

    /**
     * Saves a new ward if it does not already exist.
     *
     * @param wardBean The ward details to be saved.
     * @return The saved ward bean.
     * @throws WardAlreadyExistsException if the ward already exists.
     */
    @Override
    public WardBean saveWard(WardBean wardBean) {
        log.info("Saving the ward");

        Ward wardEntity = wardRepository.getByNameAndDepartmentId_Name(wardBean.getName(),
                wardBean.getDepartmentId().getName());
        if (wardEntity == null) {
            Ward ward = objectMapper.convertValue(wardBean, Ward.class);
            wardBean.setStatus(CommonConstants.ACTIVE);
            wardBean.setAvailability(wardBean.getCapacity());
            wardRepository.save(ward);
            log.info("Saving the ward successfully");
        } else {
            log.info("Ward already exists");
            throw new WardAlreadyExistsException("That ward already exists");
        }
        return wardBean;
    }

    /**
     * Retrieves a ward by its ID.
     *
     * @param id The ID of the ward to retrieve.
     * @return The ward bean corresponding to the given ID.
     * @throws RecordNotFoundException if no ward is found with the given ID.
     */
    @Override
    public WardBean getByWardId(Long id) {
        log.info("Getting the ward by ID");
        Ward ward = wardRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("No record found with the given ID"));
        WardBean wardBean = objectMapper.convertValue(ward, WardBean.class);
        log.info("Getting the ward by ID is done");
        return wardBean;
    }

    /**
     * Deletes a ward by its ID.
     *
     * @param id The ID of the ward to delete.
     * @throws Exception if an error occurs while deleting the ward.
     */
    @Override
    public void delete(Long id) {
        try {
            log.info("Deleting the ward");
            wardRepository.deleteById(id);
        } catch (Exception exception) {
            log.error("Error deleting ward with ID: " + id, exception);
            throw exception;
        }
    }

    /**
     * Retrieves all wards.
     *
     * @return List of ward beans.
     * @throws Exception if an error occurs while fetching wards.
     */
    @Override
    public List<WardBean> getAllWards() {
        try {
            log.info("Getting all wards");
            List<Ward> entityList = wardRepository.findAll();
            List<WardBean> beanList = new ArrayList<>();
            entityToBean(entityList, beanList);
            log.info("Getting all wards successfully");
            return beanList;
        } catch (Exception exception) {
            log.error("Error fetching all wards", exception);
            throw exception;
        }
    }

    /**
     * Converts a list of ward entities to a list of ward beans.
     *
     * @param entityList List of ward entities.
     * @param beanList   List of ward beans.
     */
    private void entityToBean(List<Ward> entityList, List<WardBean> beanList) {
        for (Ward ward : entityList) {
            WardBean wardBean = objectMapper.convertValue(ward, WardBean.class);
            beanList.add(wardBean);
        }
    }

    /**
     * Updates the details of a ward.
     *
     * @param wardBean The updated ward details.
     * @throws RecordNotFoundException if no ward is found with the given ID.
     */
    @Override
    public void updateWard(WardBean wardBean) {
        try {
            log.info("Updating the ward by ID");
            Optional<Ward> optional = wardRepository.findById(wardBean.getId());
            if (optional.isPresent()) {
                Ward ward = optional.get();
                ward.setId(wardBean.getId());
                ward.setName(wardBean.getName());
                ward.setCapacity(wardBean.getCapacity());
                ward.setAvailability(wardBean.getAvailability());
                ward.setStatus(wardBean.getStatus());
                Department department = ward.getDepartmentId();
                ward.setDepartmentId(department);
                wardRepository.save(ward);
                log.info("Updating the ward by ID is done");
            } else {
                log.info("Record not found with this ID");
                throw new RecordNotFoundException("Details not found");
            }
        } catch (Exception exception) {
            log.error("Error while updating ward", exception);
            throw exception;
        }
    }

    /**
     * Finds wards by department ID.
     *
     * @param departmentId The ID of the department.
     * @return List of wards belonging to the specified department.
     * @throws DepartmentNotFoundException if the department ID is not found.
     */
    @Override
    public List<Ward> findByDepartmentId(Long departmentId) {
        if (departmentId != null) {
            log.info("Fetching wards by department ID");
            return wardRepository.findByDepartmentId_Id(departmentId);
        } else {
            log.info("Department ID not found");
            throw new DepartmentNotFoundException("Department ID not found");
        }
    }

    /**
     * Updates the status of a ward (Active/Inactive).
     *
     * @param ward The ward entity whose status is to be updated.
     */
    @Override
    public void updateStatus(Ward ward) {
        log.info("Updating the ward status");
        if (ward.getStatus().equalsIgnoreCase(CommonConstants.ACTIVE)) {
            ward.setStatus(CommonConstants.INACTIVE);
        } else {
            ward.setStatus(CommonConstants.ACTIVE);
        }
        wardRepository.save(ward);
        log.info("Updating the ward status is done");
    }
}
