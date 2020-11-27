import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { Fun4FundTestModule } from '../../../test.module';
import { ConfigSystemComponent } from 'app/entities/config-system/config-system.component';
import { ConfigSystemService } from 'app/entities/config-system/config-system.service';
import { ConfigSystem } from 'app/shared/model/config-system.model';

describe('Component Tests', () => {
  describe('ConfigSystem Management Component', () => {
    let comp: ConfigSystemComponent;
    let fixture: ComponentFixture<ConfigSystemComponent>;
    let service: ConfigSystemService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Fun4FundTestModule],
        declarations: [ConfigSystemComponent],
      })
        .overrideTemplate(ConfigSystemComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ConfigSystemComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ConfigSystemService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new ConfigSystem(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.configSystems && comp.configSystems[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
