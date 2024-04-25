import { Component } from '@angular/core';
import { Room } from '../../Model/room.model';
import { Bed } from '../../Model/bed.model';
import { BedService } from '../services/bed.service';
import Swal from 'sweetalert2';
import { Router } from '@angular/router';


@Component({
  selector: 'app-addbed',
  templateUrl: './addbed.component.html',
  styleUrl: './addbed.component.css'
})
export class AddbedComponent {
  rooms: Room[] = [];
  newBed: Bed = { id: null, bedNo: null, status: '', roomId: null };

  constructor(private bedService: BedService,private router:Router) {}

  ngOnInit(): void {
    this.loadRooms();
  }

  loadRooms(): void {
    this.bedService.getAllRooms().subscribe(
      (rooms) => {
        this.rooms = rooms.filter((item) => item.status === 'Active');
      },
      (error) => {
        console.error('Error loading rooms:', error);
      }
    );
  }

  submitNewBed(bedForm:any): void {
    this.bedService.saveBed(this.newBed).subscribe(
      () => {
        Swal.fire({
          title: 'Saved!',
          text: 'Bed added successfully.',
          icon: 'success',
          confirmButtonText: 'Ok',
        });
        
        this.newBed = { id: null, bedNo: null, status: '', roomId: null };
       this.router.navigate(['dashboard/admin/bed']);
      },
      (error) => {
        Swal.fire({
          title: 'Ooops!',
          text: 'Room you have choosen already filled completely',
          icon: 'success',
          confirmButtonText: 'Ok',
        });
      }
    );
  }

}
