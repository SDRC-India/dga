import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CrossTabReportComponent } from './cross-tab-report.component';

describe('CrossTabReportComponent', () => {
  let component: CrossTabReportComponent;
  let fixture: ComponentFixture<CrossTabReportComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CrossTabReportComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CrossTabReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
