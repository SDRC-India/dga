import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FipComponent } from './fip.component';

describe('FipComponent', () => {
  let component: FipComponent;
  let fixture: ComponentFixture<FipComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FipComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FipComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
