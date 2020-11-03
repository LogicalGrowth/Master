import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { Fun4FundTestModule } from '../../../test.module';
import { PartnerRequestDetailComponent } from 'app/entities/partner-request/partner-request-detail.component';
import { PartnerRequest } from 'app/shared/model/partner-request.model';

describe('Component Tests', () => {
  describe('PartnerRequest Management Detail Component', () => {
    let comp: PartnerRequestDetailComponent;
    let fixture: ComponentFixture<PartnerRequestDetailComponent>;
    const route = ({ data: of({ partnerRequest: new PartnerRequest(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Fun4FundTestModule],
        declarations: [PartnerRequestDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(PartnerRequestDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PartnerRequestDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load partnerRequest on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.partnerRequest).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
