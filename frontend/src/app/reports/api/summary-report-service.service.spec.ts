import { TestBed } from '@angular/core/testing';

import { SummaryReportService } from './summary-report-service.service';

describe('SummaryReportServiceService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: SummaryReportService = TestBed.get(SummaryReportService);
    expect(service).toBeTruthy();
  });
});
