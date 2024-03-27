/**
 * This class defines REST API endpoints for managing departments.
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

import com.admin.bean.DepartmentBean;
import com.admin.entity.Department;
import com.admin.service.DepartmentService;

@RestController
@RequestMapping("/department")
@CrossOrigin(origins = "**")

public class DepartmentController {

    @Autowired
    DepartmentService departmentService;
    private static Logger log = LoggerFactory.getLogger(DepartmentController.class.getSimpleName());

    /**
     * Endpoint to save a new department.
     *
     * @param department The department details to be saved.
     * @return ResponseEntity with the saved department details.
     */
    @PostMapping("/save")
    public ResponseEntity<DepartmentBean> saveDepartment(@RequestBody DepartmentBean department) {
        log.info("Saving Department entity");
        departmentService.save(department);
        ResponseEntity<DepartmentBean> responseEntity = new ResponseEntity<>(department, HttpStatus.CREATED);
        log.info("Saving Department entity is done");
        return responseEntity;
    }

    /**
     * Endpoint to retrieve department details by ID.
     *
     * @param id The ID of the department to retrieve.
     * @return ResponseEntity with the retrieved department details.
     */
    @GetMapping("/getById/{id}")
    public ResponseEntity<DepartmentBean> getDepartmentById(@PathVariable long id) {
        log.info("Retrieving Department by ID");
        DepartmentBean departmentBean = departmentService.getById(id);
        log.info("Retrieved department by ID successfully");
        return ResponseEntity.status(HttpStatus.OK).body(departmentBean);
    }

    /**
     * Endpoint to retrieve all department details.
     *
     * @return ResponseEntity with the list of all department details.
     */
    @GetMapping("/getAll")
    public ResponseEntity<List<DepartmentBean>> getAllDepartments() {
        log.info("Retrieving All Departments");
        List<DepartmentBean> list = departmentService.getAll();
        ResponseEntity<List<DepartmentBean>> responseEntity = new ResponseEntity<>(list, HttpStatus.OK);
        log.info("Fetching All Department details is done");
        return responseEntity;
    }

    /**
     * Endpoint to delete a department by ID.
     *
     * @param id The ID of the department to delete.
     * @return ResponseEntity indicating the success of the delete operation.
     */
    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<String> deleteDepartmentById(@PathVariable long id) {
        log.info("Deleting Department by ID");
        departmentService.delete(id);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.OK);
        log.info("Deleted Department by ID successfully");
        return responseEntity;
    }

    /**
     * Endpoint to update department details.
     *
     * @param department The updated department details.
     * @param id         The ID of the department to update.
     * @return ResponseEntity indicating the success of the update operation.
     * @throws Exception if an error occurs during the update process.
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateDepartmentDetails(@RequestBody Department department, @PathVariable long id)
            throws Exception {
        log.info("Updating Department");
        DepartmentBean department1 = departmentService.getById(id);
        if (department1 != null) {
            department1.setName(department.getName());
            departmentService.save(department1);
        }
        ResponseEntity<String> responseEntity = new ResponseEntity<>("Department updated successfully", HttpStatus.OK);
        log.info("Updating department is done");
        return responseEntity;
    }

    /**
     * Endpoint to update the status of a department.
     *
     * @param department The department entity whose status is to be updated.
     */
    @PutMapping("/updateStatus")
    public void updateStatus(@RequestBody Department department) {
        log.info("Update the department status");
        departmentService.updateStatus(department);
        log.info("Updating department status is done");
    }
}
