import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { Room } from '../../Model/room.model';
import { RoomType } from '../../Model/roomkind.model';
import { Ward } from '../../Model/ward.model';
import { RoomService } from '../services/room.service';

@Component({
  selector: 'app-room',
  templateUrl: './room.component.html',
  styleUrls: ['./room.component.css'],
})
export class RoomComponent implements OnInit {
  public combinedDetails: Room[] = [];
  public roomKind: RoomType[] = [];
  public wards: Ward[] = [];
  public pageSize: number = 4;
  public currentPage = 1;
  public totalItems = 0;

  roomFormtype!: FormGroup;
  displayedColumns: string[] = ['roomNo', 'roomSharing', 'roomPrice', 'roomType', 'ward', 'availability', 'status', 'actions'];
  constructor(
    private fb: FormBuilder,
    private service: RoomService,
    private router: Router,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.createForm();
    this.getDetails();
    this.roomType();
    this.getWards();
  }

  createForm(): void {
    this.roomFormtype = this.fb.group({
      roomNo: ['', Validators.required],
      roomSharing: ['', Validators.required],
      roomPrice: ['', Validators.required],
      roomTypeId: ['', Validators.required],
      wardId: ['', Validators.required],
      availability: ['', Validators.required],
      status: ['', Validators.required],
    });
  }

  roomType(): void {
    this.service.getAllRoomTypes().subscribe((data) => {
      this.roomKind = data.filter((item) => item.status === 'Active');
    });
  }

  getWards(): void {
    // Assuming you have a method in your RoomService to fetch wards
    this.service.getAllByWard ().subscribe((data) => {
      this.wards = data;
    });
  }

  getDetails(): void {
    this.service.getAllDetails(this.pageSize, this.currentPage).subscribe((data) => {
      this.combinedDetails = data;
      this.totalItems = data.length;
      this.combinedDetails = data.slice(
        (this.currentPage - 1) * this.pageSize,
        this.currentPage * this.pageSize
      );
    });
  }

  delete(room: Room): void {
    this.service.deleteRoom(room).subscribe(() => {
      console.log('Deleted successfully');
      this.getDetails(); // Refresh data after deletion
    });
  }

  onPageChange(event: number): void {
    if (event >= 1 && event <= Math.ceil(this.totalItems / this.pageSize)) {
      this.currentPage = event;
      this.getDetails();
    }
  }

  isPreviousButtonDisabled(): boolean {
    return this.currentPage === 1;
  }

  isNextButtonDisabled(): boolean {
    return this.currentPage === Math.ceil(this.totalItems / this.pageSize);
  }
}