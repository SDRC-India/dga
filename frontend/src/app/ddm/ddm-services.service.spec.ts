import { TestBed } from '@angular/core/testing';

import { DdmServicesService } from './ddm-services.service';

describe('DdmServicesService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: DdmServicesService = TestBed.get(DdmServicesService);
    expect(service).toBeTruthy();
  });
});
