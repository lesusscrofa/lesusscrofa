import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientCreationComponent } from './client-creation.component';

describe('ClientDetailComponent', () => {
  let component: ClientCreationComponent;
  let fixture: ComponentFixture<ClientCreationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ClientCreationComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ClientCreationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
