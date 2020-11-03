import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { Fun4FundTestModule } from '../../../test.module';
import { DonationHistoryDetailComponent } from 'app/entities/donation-history/donation-history-detail.component';
import { DonationHistory } from 'app/shared/model/donation-history.model';

describe('Component Tests', () => {
  describe('DonationHistory Management Detail Component', () => {
    let comp: DonationHistoryDetailComponent;
    let fixture: ComponentFixture<DonationHistoryDetailComponent>;
    const route = ({ data: of({ donationHistory: new DonationHistory(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Fun4FundTestModule],
        declarations: [DonationHistoryDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(DonationHistoryDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DonationHistoryDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load donationHistory on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.donationHistory).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
