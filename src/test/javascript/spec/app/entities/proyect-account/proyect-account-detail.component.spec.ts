import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { Fun4FundTestModule } from '../../../test.module';
import { ProyectAccountDetailComponent } from 'app/entities/proyect-account/proyect-account-detail.component';
import { ProyectAccount } from 'app/shared/model/proyect-account.model';

describe('Component Tests', () => {
  describe('ProyectAccount Management Detail Component', () => {
    let comp: ProyectAccountDetailComponent;
    let fixture: ComponentFixture<ProyectAccountDetailComponent>;
    const route = ({ data: of({ proyectAccount: new ProyectAccount(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Fun4FundTestModule],
        declarations: [ProyectAccountDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(ProyectAccountDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ProyectAccountDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load proyectAccount on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.proyectAccount).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
