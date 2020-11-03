import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { Fun4FundTestModule } from '../../../test.module';
import { ProyectDetailComponent } from 'app/entities/proyect/proyect-detail.component';
import { Proyect } from 'app/shared/model/proyect.model';

describe('Component Tests', () => {
  describe('Proyect Management Detail Component', () => {
    let comp: ProyectDetailComponent;
    let fixture: ComponentFixture<ProyectDetailComponent>;
    const route = ({ data: of({ proyect: new Proyect(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Fun4FundTestModule],
        declarations: [ProyectDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(ProyectDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ProyectDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load proyect on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.proyect).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
