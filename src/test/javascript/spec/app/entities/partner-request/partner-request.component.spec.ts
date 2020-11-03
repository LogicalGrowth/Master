import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { Fun4FundTestModule } from '../../../test.module';
import { PartnerRequestComponent } from 'app/entities/partner-request/partner-request.component';
import { PartnerRequestService } from 'app/entities/partner-request/partner-request.service';
import { PartnerRequest } from 'app/shared/model/partner-request.model';

describe('Component Tests', () => {
  describe('PartnerRequest Management Component', () => {
    let comp: PartnerRequestComponent;
    let fixture: ComponentFixture<PartnerRequestComponent>;
    let service: PartnerRequestService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Fun4FundTestModule],
        declarations: [PartnerRequestComponent],
      })
        .overrideTemplate(PartnerRequestComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PartnerRequestComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PartnerRequestService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new PartnerRequest(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.partnerRequests && comp.partnerRequests[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
