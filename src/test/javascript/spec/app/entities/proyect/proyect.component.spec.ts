import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { Fun4FundTestModule } from '../../../test.module';
import { ProyectComponent } from 'app/entities/proyect/proyect.component';
import { ProyectService } from 'app/entities/proyect/proyect.service';
import { Proyect } from 'app/shared/model/proyect.model';

describe('Component Tests', () => {
  describe('Proyect Management Component', () => {
    let comp: ProyectComponent;
    let fixture: ComponentFixture<ProyectComponent>;
    let service: ProyectService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Fun4FundTestModule],
        declarations: [ProyectComponent],
      })
        .overrideTemplate(ProyectComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProyectComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ProyectService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Proyect(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.proyects && comp.proyects[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
