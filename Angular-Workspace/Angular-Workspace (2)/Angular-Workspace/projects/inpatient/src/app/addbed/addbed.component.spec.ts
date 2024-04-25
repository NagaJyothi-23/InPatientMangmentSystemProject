import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddbedComponent } from './addbed.component';

describe('AddbedComponent', () => {
  let component: AddbedComponent;
  let fixture: ComponentFixture<AddbedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AddbedComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AddbedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
