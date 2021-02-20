import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BillOrderListComponent } from './bill-order-list.component';

describe('BillOrderListComponent', () => {
  let component: BillOrderListComponent;
  let fixture: ComponentFixture<BillOrderListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BillOrderListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BillOrderListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
