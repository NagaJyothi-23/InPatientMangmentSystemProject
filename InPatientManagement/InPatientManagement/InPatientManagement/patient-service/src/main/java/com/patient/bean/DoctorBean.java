package com.patient.bean;

public class DoctorBean {

	private long id;
	private String name;
	private long departmentId;
	private String status;

	public DoctorBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DoctorBean(long id, String name, long departmentId, String status) {
		super();
		this.id = id;
		this.name = name;
		this.departmentId = departmentId;
		this.status = status;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(long departmentId) {
		this.departmentId = departmentId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "DoctorBean [id=" + id + ", name=" + name + ", departmentId=" + departmentId + ", status=" + status
				+ "]";
	}

}
