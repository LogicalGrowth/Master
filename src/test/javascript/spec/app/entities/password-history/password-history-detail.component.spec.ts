import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { Fun4FundTestModule } from '../../../test.module';
import { PasswordHistoryDetailComponent } from 'app/entities/password-history/password-history-detail.component';
import { PasswordHistory } from 'app/shared/model/password-history.model';

describe('Component Tests', () => {
  describe('PasswordHistory Management Detail Component', () => {
    let comp: PasswordHistoryDetailComponent;
    let fixture: ComponentFixture<PasswordHistoryDetailComponent>;
    const route = ({ data: of({ passwordHistory: new PasswordHistory(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Fun4FundTestModule],
        declarations: [PasswordHistoryDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(PasswordHistoryDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PasswordHistoryDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load passwordHistory on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.passwordHistory).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
