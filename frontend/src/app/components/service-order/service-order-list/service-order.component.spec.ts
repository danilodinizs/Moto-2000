import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ServiceOrderComponent } from './service-order.component';

describe('ServiceOrderComponent', () => {
  let component: ServiceOrderComponent;
  let fixture: ComponentFixture<ServiceOrderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ServiceOrderComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ServiceOrderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
