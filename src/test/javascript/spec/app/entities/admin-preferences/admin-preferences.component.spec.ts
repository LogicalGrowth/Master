import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { Fun4FundTestModule } from '../../../test.module';
import { AdminPreferencesComponent } from 'app/entities/admin-preferences/admin-preferences.component';
import { AdminPreferencesService } from 'app/entities/admin-preferences/admin-preferences.service';
import { AdminPreferences } from 'app/shared/model/admin-preferences.model';

describe('Component Tests', () => {
  describe('AdminPreferences Management Component', () => {
    let comp: AdminPreferencesComponent;
    let fixture: ComponentFixture<AdminPreferencesComponent>;
    let service: AdminPreferencesService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Fun4FundTestModule],
        declarations: [AdminPreferencesComponent],
      })
        .overrideTemplate(AdminPreferencesComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AdminPreferencesComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(AdminPreferencesService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new AdminPreferences(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.adminPreferences && comp.adminPreferences[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
