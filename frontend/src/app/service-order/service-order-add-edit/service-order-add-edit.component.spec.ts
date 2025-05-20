import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ServiceOrderAddEditComponent } from './service-order-add-edit.component';

describe('ServiceOrderAddEditComponent', () => {
  let component: ServiceOrderAddEditComponent;
  let fixture: ComponentFixture<ServiceOrderAddEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ServiceOrderAddEditComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ServiceOrderAddEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
