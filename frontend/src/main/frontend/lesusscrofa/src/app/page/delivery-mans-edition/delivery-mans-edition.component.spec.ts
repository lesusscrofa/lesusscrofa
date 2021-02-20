import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeliveryMansEditionComponent } from './delivery-mans-edition.component';

describe('DeliveryMansEditionComponent', () => {
  let component: DeliveryMansEditionComponent;
  let fixture: ComponentFixture<DeliveryMansEditionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DeliveryMansEditionComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DeliveryMansEditionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
