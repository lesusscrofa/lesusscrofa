import { ComponentFixture, TestBed } from '@angular/core/testing';
import { DeliveryManEditionComponent } from './delivery-man-edition.component';

describe('PriceEditionComponent', () => {
  let component: DeliveryManEditionComponent;
  let fixture: ComponentFixture<DeliveryManEditionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DeliveryManEditionComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DeliveryManEditionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
