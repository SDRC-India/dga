import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DdmReportComponent } from './ddm-report.component';

describe('DdmReportComponent', () => {
  let component: DdmReportComponent;
  let fixture: ComponentFixture<DdmReportComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DdmReportComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DdmReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
