import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Patient } from '../../Model/patient.module';
import { Department } from '../../Model/department.model';

@Injectable({
  providedIn: 'root',
})
export class PatientService {
  [x: string]: any;
  constructor(private http: HttpClient) {}

  getAllPatient(): Observable<Patient[]> {
    return this.http.get<Patient[]>(
      'http://13.48.82.196:8103/patient-service/patients/getAll'
    );
  }

  savePatient(patient: Patient): Observable<Patient> {
    return this.http.post<Patient>(
      'http://13.48.82.196:8103/patient-service/patients/save',
      patient
    );
  }

  updatePatient(id: number, patient: Patient): Observable<Patient> {
    return this.http.put<Patient>(
      `http://13.48.82.196:8103/patient-service/patients/${id}`,
      patient
    );
  }
  getAlldoctor(): Observable<any[]> {
    return this.http.get<any[]>(
      'http://13.48.82.196:8103/patient-service/doctor/getAll'
    );
  }
  getAllDepartments(): Observable<Department[]> {
    return this.http.get<Department[]>(
      'http://13.48.82.196:8101/admin-service/department/getAll'
    );
  }
  private currentYear!: number;
  private currentMonth!: number;
  private currentNumber!: number;

  generatePatientNo(): string {
    const currentDate = new Date();
    const year = currentDate.getFullYear();
    const month = currentDate.getMonth() + 1;

    if (year !== this.currentYear || month !== this.currentMonth) {
      this.currentYear = year;
      this.currentMonth = month;
      this.currentNumber = 1;
    } else {
      this.currentNumber++;
    }

    return `IN-${(this.currentYear % 100)
      .toString()
      .padStart(2, '0')}-${this.currentMonth
      .toString()
      .padStart(2, '0')}-${this.currentNumber.toString().padStart(4, '0')}`;
  }
}
