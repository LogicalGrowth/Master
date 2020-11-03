import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { Fun4FundTestModule } from '../../../test.module';
import { AppLogComponent } from 'app/entities/app-log/app-log.component';
import { AppLogService } from 'app/entities/app-log/app-log.service';
import { AppLog } from 'app/shared/model/app-log.model';

describe('Component Tests', () => {
  describe('AppLog Management Component', () => {
    let comp: AppLogComponent;
    let fixture: ComponentFixture<AppLogComponent>;
    let service: AppLogService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Fun4FundTestModule],
        declarations: [AppLogComponent],
      })
        .overrideTemplate(AppLogComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AppLogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(AppLogService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new AppLog(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.appLogs && comp.appLogs[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
