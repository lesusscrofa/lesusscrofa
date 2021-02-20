import { TestBed } from '@angular/core/testing';
import { HalUtils } from './halUtils';

describe('MenuService', () => {

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(HalUtils.extractId("http://localhost:8080/menu/1")).toEqual(1);
  });
});
