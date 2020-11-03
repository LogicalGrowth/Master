import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { Fun4FundTestModule } from '../../../test.module';
import { AdminPreferencesDetailComponent } from 'app/entities/admin-preferences/admin-preferences-detail.component';
import { AdminPreferences } from 'app/shared/model/admin-preferences.model';

describe('Component Tests', () => {
  describe('AdminPreferences Management Detail Component', () => {
    let comp: AdminPreferencesDetailComponent;
    let fixture: ComponentFixture<AdminPreferencesDetailComponent>;
    const route = ({ data: of({ adminPreferences: new AdminPreferences(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Fun4FundTestModule],
        declarations: [AdminPreferencesDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(AdminPreferencesDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AdminPreferencesDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load adminPreferences on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.adminPreferences).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
