import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DistHealthActionPlanComponent } from './dist-health-action-plan.component';

describe('DistHealthActionPlanComponent', () => {
  let component: DistHealthActionPlanComponent;
  let fixture: ComponentFixture<DistHealthActionPlanComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DistHealthActionPlanComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DistHealthActionPlanComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
