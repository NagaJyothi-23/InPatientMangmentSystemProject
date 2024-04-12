import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { user } from '../../Model/user';
import { RoomtypeDetailsService } from '../services/roomtype-details.service';
import { UtilityService } from '../service/utility.service';
import Swal from 'sweetalert2';
import { AuthService } from '../services/AuthService';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent implements OnInit {
  public email: string = '';
  public password: string = '';
  public values: any[] = [];
  public details: any;
  public showPassword: boolean = true;
  public user: user = new user('', '', '', '', 0, '', '', 0, '');
  click() { }
  loginForm!: FormGroup;
  isLoggedIn: boolean = false;

  constructor(
    private fb: FormBuilder,
    private service: RoomtypeDetailsService,
    private router: Router,
    private utilityService: UtilityService,
    private authservice:AuthService,
  ) { }

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      email: this.fb.control('', [Validators.required, Validators.email]),
      password: this.fb.control('', [
        Validators.required,
        Validators.minLength(4),
      ]),
    });
  }
  togglePasswordVisibility(event: MouseEvent): void {
    event.preventDefault();
    this.showPassword = !this.showPassword;
  }
  loginData() {
    if (this.loginForm.valid) {
      const email = this.loginForm.value.email;
      const password = this.loginForm.value.password;

      this.values = this.loginForm.value;
      this.details = JSON.stringify(this.values);

      console.log(this.details);

      this.service.loginDetails(this.details).subscribe(
        (response: any) => {
          Swal.fire({
            title: 'Welcome!',
            text: 'Login successfully.',
            icon: 'success',
            confirmButtonText: 'Ok',
          });
          const token = response.accessToken;
          this.authservice.setAccessToken(token);
          this.authservice.setRole(response.registrationForm.serviceType);
          this.authservice.setUser(JSON.stringify(response.registrationForm))
          const json = response.JSON;
          if (response != null) {
            if (response.registrationForm.serviceType == 'Admin') {
              this.utilityService.loginIn(response);
              this.utilityService.role = this.user.serviceType;
              console.log('Login successful');
              this.router.navigate(['/dashboard/admin/']);
            } else if (response.registrationForm.serviceType == 'receptionist') {
              this.utilityService.loginIn(response);
              this.utilityService.role = this.user.serviceType;
              this.router.navigate(['/dashboard/receptionist/']);
            }
            //this.router.navigate(['/dashboard']);
          } else {
          }
        },
        (error: any) => {
          console.error('Error:', error);

          // Handle specific error cases if needed
          if (error.status === 401) {
            console.log('Unauthorized - Incorrect credentials');
          } else {
            console.log('Login failed - Unexpected error');
          }
        }
      );
    }
  }
}
