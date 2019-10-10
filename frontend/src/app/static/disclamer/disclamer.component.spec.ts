import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DisclamerComponent } from './disclamer.component';

describe('DisclamerComponent', () => {
  let component: DisclamerComponent;
  let fixture: ComponentFixture<DisclamerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DisclamerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DisclamerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
