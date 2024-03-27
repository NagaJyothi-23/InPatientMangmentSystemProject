package com.admin.service.implementation;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.admin.bean.DepartmentBean;
import com.admin.constants.CommonConstants;
import com.admin.entity.Department;
import com.admin.exception.DepartmentAlreadyExistsException;
import com.admin.exception.RecordNotFoundException;
import com.admin.repository.DepartmentRepository;
import com.admin.service.DepartmentService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Implementation of the DepartmentService interface.
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    private ObjectMapper objectMapper;

    private static Logger log = LoggerFactory.getLogger(DepartmentServiceImpl.class.getSimpleName());

    /**
     * Saves a new department.
     *
     * @param departmentBean The department details to be saved.
     * @return The saved department details.
     * @throws DepartmentAlreadyExistsException If a department with the same name already exists.
     */
    @Override
    public DepartmentBean save(DepartmentBean departmentBean) {
        log.info("Saving the department");

        Department dept = departmentRepository.getByName(departmentBean.getName());

        if (dept == null) {
            departmentBean.setStatus(CommonConstants.ACTIVE);
            departmentRepository.save(objectMapper.convertValue(departmentBean, Department.class));
            return departmentBean;
        } else {
            throw new DepartmentAlreadyExistsException("Department already exists");
        }
    }

    /**
     * Retrieves a department by its ID.
     *
     * @param id The ID of the department to retrieve.
     * @return The department details.
     * @throws RecordNotFoundException If no record is found with the given ID.
     */
    @Override
    public DepartmentBean getById(long id) {
        log.info("Retrieving department by ID");
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("No Record Found with given ID"));
        return objectMapper.convertValue(department, DepartmentBean.class);
    }

    /**
     * Retrieves all departments.
     *
     * @return List of all department details.
     */
    @Override
    public List<DepartmentBean> getAll() {
        try {
            log.info("Retrieving all departments");
            List<Department> list = departmentRepository.findAll();
            List<DepartmentBean> beanList = new ArrayList<>();
            entityToBean(list, beanList);
            return beanList;
        } catch (Exception exception) {
            log.error("Error occurred while fetching all departments", exception);
            throw exception;
        }
    }

    /**
     * Deletes a department by its ID.
     *
     * @param id The ID of the department to delete.
     * @throws RecordNotFoundException If no record is found with the given ID.
     */
    @Override
    public void delete(long id) {
        try {
            log.info("Deleting department by ID");
            departmentRepository.findById(id)
                    .orElseThrow(() -> new RecordNotFoundException("No Record Found with given ID"));
            departmentRepository.deleteById(id);
        } catch (RecordNotFoundException exception) {
            log.error("Error occurred while deleting department by ID", exception);
            throw exception;
        }
    }

    /**
     * Converts entity object to bean object.
     *
     * @param department     The department entity object.
     * @param departmentBean The department bean object.
     */
    public void entityToBean(Department department, DepartmentBean departmentBean) {
        departmentBean = objectMapper.convertValue(department, DepartmentBean.class);
    }

    /**
     * Converts list of entity objects to list of bean objects.
     *
     * @param list     The list of department entity objects.
     * @param beanList The list of department bean objects.
     */
    public void entityToBean(List<Department> list, List<DepartmentBean> beanList) {
        for (Department department : list) {
            DepartmentBean departmentBean = objectMapper.convertValue(department, DepartmentBean.class);
            beanList.add(departmentBean);
        }
    }

    /**
     * Updates the status of a department.
     *
     * @param department The department to update.
     */
    @Override
    public void updateStatus(Department department) {
        if (department.getStatus().equalsIgnoreCase(CommonConstants.ACTIVE)) {
            department.setStatus(CommonConstants.INACTIVE);
        } else {
            department.setStatus(CommonConstants.ACTIVE);
        }
        departmentRepository.save(department);
    }
}
