import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OrderFoodFieldComponent } from './order-food-field.component';

describe('OrderFoodComponentComponent', () => {
  let component: OrderFoodFieldComponent;
  let fixture: ComponentFixture<OrderFoodFieldComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OrderFoodFieldComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OrderFoodFieldComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
