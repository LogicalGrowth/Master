import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { Fun4FundTestModule } from '../../../test.module';
import { ResourceComponent } from 'app/entities/resource/resource.component';
import { ResourceService } from 'app/entities/resource/resource.service';
import { Resource } from 'app/shared/model/resource.model';

describe('Component Tests', () => {
  describe('Resource Management Component', () => {
    let comp: ResourceComponent;
    let fixture: ComponentFixture<ResourceComponent>;
    let service: ResourceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Fun4FundTestModule],
        declarations: [ResourceComponent],
      })
        .overrideTemplate(ResourceComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ResourceComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ResourceService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Resource(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.resources && comp.resources[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
