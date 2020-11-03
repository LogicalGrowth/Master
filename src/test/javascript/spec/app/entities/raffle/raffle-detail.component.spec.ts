import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { Fun4FundTestModule } from '../../../test.module';
import { RaffleDetailComponent } from 'app/entities/raffle/raffle-detail.component';
import { Raffle } from 'app/shared/model/raffle.model';

describe('Component Tests', () => {
  describe('Raffle Management Detail Component', () => {
    let comp: RaffleDetailComponent;
    let fixture: ComponentFixture<RaffleDetailComponent>;
    const route = ({ data: of({ raffle: new Raffle(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Fun4FundTestModule],
        declarations: [RaffleDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(RaffleDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(RaffleDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load raffle on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.raffle).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
