import { TestBed } from '@angular/core/testing';

import { XhrInterceptorServiceService } from './xhr-interceptor-service.service';

describe('XhrInterceptorServiceService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: XhrInterceptorServiceService = TestBed.get(XhrInterceptorServiceService);
    expect(service).toBeTruthy();
  });
});
