import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { Fun4FundTestModule } from '../../../test.module';
import { ConfigSystemDetailComponent } from 'app/entities/config-system/config-system-detail.component';
import { ConfigSystem } from 'app/shared/model/config-system.model';

describe('Component Tests', () => {
  describe('ConfigSystem Management Detail Component', () => {
    let comp: ConfigSystemDetailComponent;
    let fixture: ComponentFixture<ConfigSystemDetailComponent>;
    const route = ({ data: of({ configSystem: new ConfigSystem(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Fun4FundTestModule],
        declarations: [ConfigSystemDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(ConfigSystemDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ConfigSystemDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load configSystem on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.configSystem).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
