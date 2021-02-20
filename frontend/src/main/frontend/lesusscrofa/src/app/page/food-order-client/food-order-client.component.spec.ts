import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FoodOrderClientComponent } from './food-order-client.component';

describe('FoodOrderClientComponent', () => {
  let component: FoodOrderClientComponent;
  let fixture: ComponentFixture<FoodOrderClientComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FoodOrderClientComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FoodOrderClientComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
