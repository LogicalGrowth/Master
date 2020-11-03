import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { Fun4FundTestModule } from '../../../test.module';
import { AuctionDetailComponent } from 'app/entities/auction/auction-detail.component';
import { Auction } from 'app/shared/model/auction.model';

describe('Component Tests', () => {
  describe('Auction Management Detail Component', () => {
    let comp: AuctionDetailComponent;
    let fixture: ComponentFixture<AuctionDetailComponent>;
    const route = ({ data: of({ auction: new Auction(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Fun4FundTestModule],
        declarations: [AuctionDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(AuctionDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AuctionDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load auction on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.auction).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
