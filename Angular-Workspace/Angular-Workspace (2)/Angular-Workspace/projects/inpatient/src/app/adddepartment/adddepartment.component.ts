import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { DepartmentService } from '../services/department.service';
import Swal from 'sweetalert2';
import { Router } from '@angular/router';


@Component({
  selector: 'app-adddepartment',
  templateUrl: './adddepartment.component.html',
  styleUrl: './adddepartment.component.css'
})
export class AdddepartmentComponent {
  newDepartmentName: string = '';

  constructor(private departmentService: DepartmentService,private router:Router) {}

  submitNewDepartment(departmentForm: NgForm): void {
    // Check if department name is provided
    if (!this.newDepartmentName.trim()) {
      // Handle error: Department name is required
      return;
    }

    const regex = /^[A-Za-z]+$/;
    if (!regex.test(this.newDepartmentName)) {
      Swal.fire({
        title: 'Ooops!',
        text: 'Failed to save. Please enter only characters.',
        icon: 'error',
        confirmButtonText: 'Ok',
      });
      return;
    }

    this.departmentService.saveDepartment({ id: null, name: this.newDepartmentName, status: '' }).subscribe(
      () => {
        Swal.fire({
          title: 'Saved!',
          text: 'Department added successfully.',
          icon: 'success',
          confirmButtonText: 'Ok',
        });
        // Reset the form after successful submission
        departmentForm.resetForm();
        this.router.navigate(['dashboard/admin/department']);

      },
      (error) => {
        Swal.fire({
          title: 'Oops!',
          text: 'Department already exists.',
          icon: 'error',
          confirmButtonText: 'Ok',
        });
      }
    );
  }
}
