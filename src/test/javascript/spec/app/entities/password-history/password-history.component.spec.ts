import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { Fun4FundTestModule } from '../../../test.module';
import { PasswordHistoryComponent } from 'app/entities/password-history/password-history.component';
import { PasswordHistoryService } from 'app/entities/password-history/password-history.service';
import { PasswordHistory } from 'app/shared/model/password-history.model';

describe('Component Tests', () => {
  describe('PasswordHistory Management Component', () => {
    let comp: PasswordHistoryComponent;
    let fixture: ComponentFixture<PasswordHistoryComponent>;
    let service: PasswordHistoryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Fun4FundTestModule],
        declarations: [PasswordHistoryComponent],
      })
        .overrideTemplate(PasswordHistoryComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PasswordHistoryComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PasswordHistoryService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new PasswordHistory(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.passwordHistories && comp.passwordHistories[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
