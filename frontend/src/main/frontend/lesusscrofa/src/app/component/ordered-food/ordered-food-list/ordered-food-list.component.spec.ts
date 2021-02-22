import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OrderedFoodListComponent } from './ordered-food-list.component';

describe('FoodOrderListComponent', () => {
  let component: OrderedFoodListComponent;
  let fixture: ComponentFixture<OrderedFoodListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OrderedFoodListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OrderedFoodListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
