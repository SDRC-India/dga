import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RawDataReportComponent } from './raw-data-report.component';

describe('RawDataReportComponent', () => {
  let component: RawDataReportComponent;
  let fixture: ComponentFixture<RawDataReportComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RawDataReportComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RawDataReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
