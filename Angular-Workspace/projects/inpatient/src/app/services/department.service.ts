import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Department } from '../../Model/department.model';

@Injectable({
  providedIn: 'root',
})
export class DepartmentService {
  private apiUrl = 'http://13.48.82.196:8101/admin-service/department';

  constructor(private http: HttpClient) {}

  getAllDepartments(
    pageSize: number,
    currentPage: number
  ): Observable<Department[]> {
    const params = new HttpParams()
      .set('_page', currentPage.toString())
      .set('_limit', pageSize.toString());
    return this.http.get<Department[]>(
      'http://13.48.82.196:8101/admin-service/department/getAll',
      { params }
    );
  }

  saveDepartment(department: Department): Observable<Department> {
    return this.http.post<Department>(
      `http://13.48.82.196:8101/admin-service/department/save`,
      department
    );
  }

  updateDepartment(id: number, department: Department): Observable<Department> {
    return this.http.put<Department>(
      `http://13.48.82.196:8083/admin-service/department/${id}`,
      department
    );
  }

  deleteDepartment(department: Department): Observable<void> {
    return this.http.put<void>(
      `http://13.48.82.196:8083/admin-service/department/updateStatus`,
      department
    );
  }
}
