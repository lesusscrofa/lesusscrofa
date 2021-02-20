import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FoodOrderQuantityEditionComponent } from './food-order-quantity-edition.component';

describe('FoodOrderQuantityEditionComponent', () => {
  let component: FoodOrderQuantityEditionComponent;
  let fixture: ComponentFixture<FoodOrderQuantityEditionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FoodOrderQuantityEditionComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FoodOrderQuantityEditionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
