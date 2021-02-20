import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeliveryZonesEditionComponent } from './delivery-zones-edition.component';

describe('DeliveryZonesEditionComponent', () => {
  let component: DeliveryZonesEditionComponent;
  let fixture: ComponentFixture<DeliveryZonesEditionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DeliveryZonesEditionComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DeliveryZonesEditionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
