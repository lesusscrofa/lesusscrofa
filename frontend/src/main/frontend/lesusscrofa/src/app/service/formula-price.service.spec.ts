import { TestBed } from '@angular/core/testing';

import { FormulaPriceService } from './formula-price.service';

describe('FormulaPriceService', () => {
  let service: FormulaPriceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FormulaPriceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
