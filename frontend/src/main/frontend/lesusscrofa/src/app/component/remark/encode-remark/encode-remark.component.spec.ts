import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EncodeRemarkComponent } from './encode-remark.component';

describe('EncodeRemarkComponent', () => {
  let component: EncodeRemarkComponent;
  let fixture: ComponentFixture<EncodeRemarkComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EncodeRemarkComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EncodeRemarkComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
