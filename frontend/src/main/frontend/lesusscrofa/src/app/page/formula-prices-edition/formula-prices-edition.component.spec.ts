import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormulaPricesEditionComponent } from './formula-prices-edition.component';

describe('PriceEditionComponent', () => {
  let component: FormulaPricesEditionComponent;
  let fixture: ComponentFixture<FormulaPricesEditionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FormulaPricesEditionComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FormulaPricesEditionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
