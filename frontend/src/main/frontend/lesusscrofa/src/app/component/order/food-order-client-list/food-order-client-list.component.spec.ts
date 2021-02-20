import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FoodOrderClientListComponent } from './food-order-client-list.component';

describe('FoodOrderClientListComponent', () => {
  let component: FoodOrderClientListComponent;
  let fixture: ComponentFixture<FoodOrderClientListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FoodOrderClientListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FoodOrderClientListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
