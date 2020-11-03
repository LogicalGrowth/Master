import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { Fun4FundTestModule } from '../../../test.module';
import { AuctionComponent } from 'app/entities/auction/auction.component';
import { AuctionService } from 'app/entities/auction/auction.service';
import { Auction } from 'app/shared/model/auction.model';

describe('Component Tests', () => {
  describe('Auction Management Component', () => {
    let comp: AuctionComponent;
    let fixture: ComponentFixture<AuctionComponent>;
    let service: AuctionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Fun4FundTestModule],
        declarations: [AuctionComponent],
      })
        .overrideTemplate(AuctionComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AuctionComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(AuctionService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Auction(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.auctions && comp.auctions[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
