import { TestBed } from '@angular/core/testing';

import { OrderedFoodService } from './ordered-food.service';

describe('FoodOrderService', () => {
  let service: OrderedFoodService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OrderedFoodService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
