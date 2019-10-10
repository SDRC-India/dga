import { TestBed } from '@angular/core/testing';

import { CrossTabReportServiceService } from './cross-tab-report-service.service';

describe('CrossTabReportServiceService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: CrossTabReportServiceService = TestBed.get(CrossTabReportServiceService);
    expect(service).toBeTruthy();
  });
});
