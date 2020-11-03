import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { Fun4FundTestModule } from '../../../test.module';
import { DonationHistoryComponent } from 'app/entities/donation-history/donation-history.component';
import { DonationHistoryService } from 'app/entities/donation-history/donation-history.service';
import { DonationHistory } from 'app/shared/model/donation-history.model';

describe('Component Tests', () => {
  describe('DonationHistory Management Component', () => {
    let comp: DonationHistoryComponent;
    let fixture: ComponentFixture<DonationHistoryComponent>;
    let service: DonationHistoryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Fun4FundTestModule],
        declarations: [DonationHistoryComponent],
      })
        .overrideTemplate(DonationHistoryComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DonationHistoryComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DonationHistoryService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new DonationHistory(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.donationHistories && comp.donationHistories[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
