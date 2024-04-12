package com.patient.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatientBean {
	private int patientId;
	private String firstName;
	private String lastName;
	private String patientGender;
	private int patientAge;
	private long patientContactNo;
	private long patientAlternteContactNo;
	private String patientNumber;
	private DoctorBean doctor;
	private String status;
}
