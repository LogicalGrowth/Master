import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { Fun4FundTestModule } from '../../../test.module';
import { AppLogDetailComponent } from 'app/entities/app-log/app-log-detail.component';
import { AppLog } from 'app/shared/model/app-log.model';

describe('Component Tests', () => {
  describe('AppLog Management Detail Component', () => {
    let comp: AppLogDetailComponent;
    let fixture: ComponentFixture<AppLogDetailComponent>;
    const route = ({ data: of({ appLog: new AppLog(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Fun4FundTestModule],
        declarations: [AppLogDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(AppLogDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AppLogDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load appLog on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.appLog).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
