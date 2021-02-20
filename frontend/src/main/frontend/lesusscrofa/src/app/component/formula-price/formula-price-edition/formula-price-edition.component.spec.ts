import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormulaPriceEditionComponent } from './formula-price-edition.component';

describe('PriceEditionComponent', () => {
  let component: FormulaPriceEditionComponent;
  let fixture: ComponentFixture<FormulaPriceEditionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FormulaPriceEditionComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FormulaPriceEditionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
