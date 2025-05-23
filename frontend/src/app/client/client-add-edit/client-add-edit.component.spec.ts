import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientAddEditComponent } from './client-add-edit.component';

describe('ClientAddEditComponent', () => {
  let component: ClientAddEditComponent;
  let fixture: ComponentFixture<ClientAddEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClientAddEditComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ClientAddEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
