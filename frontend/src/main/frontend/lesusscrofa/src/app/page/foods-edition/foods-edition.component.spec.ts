import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FoodsEditionComponent } from './foods-edition.component';

describe('FoodsEditionComponent', () => {
  let component: FoodsEditionComponent;
  let fixture: ComponentFixture<FoodsEditionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FoodsEditionComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FoodsEditionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
