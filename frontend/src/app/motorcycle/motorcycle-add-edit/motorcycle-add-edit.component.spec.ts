import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MotorcycleAddEditComponent } from './motorcycle-add-edit.component';

describe('MotorcycleAddEditComponent', () => {
  let component: MotorcycleAddEditComponent;
  let fixture: ComponentFixture<MotorcycleAddEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MotorcycleAddEditComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MotorcycleAddEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
