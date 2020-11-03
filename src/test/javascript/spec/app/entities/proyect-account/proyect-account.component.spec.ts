import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { Fun4FundTestModule } from '../../../test.module';
import { ProyectAccountComponent } from 'app/entities/proyect-account/proyect-account.component';
import { ProyectAccountService } from 'app/entities/proyect-account/proyect-account.service';
import { ProyectAccount } from 'app/shared/model/proyect-account.model';

describe('Component Tests', () => {
  describe('ProyectAccount Management Component', () => {
    let comp: ProyectAccountComponent;
    let fixture: ComponentFixture<ProyectAccountComponent>;
    let service: ProyectAccountService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Fun4FundTestModule],
        declarations: [ProyectAccountComponent],
      })
        .overrideTemplate(ProyectAccountComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProyectAccountComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ProyectAccountService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new ProyectAccount(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.proyectAccounts && comp.proyectAccounts[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
