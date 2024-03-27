import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
@Injectable({
  providedIn: 'root',
})
export class EnquiryService {
  constructor(private http: HttpClient) {}

  getDetails(name: string): Observable<any[]> {
    return this.http.get<any[]>(
      `http://13.48.82.196:8103/patient-service/patients/getByName/${name}`
    );
  }
}
