import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { Fun4FundTestModule } from '../../../test.module';
import { ExclusiveContentComponent } from 'app/entities/exclusive-content/exclusive-content.component';
import { ExclusiveContentService } from 'app/entities/exclusive-content/exclusive-content.service';
import { ExclusiveContent } from 'app/shared/model/exclusive-content.model';

describe('Component Tests', () => {
  describe('ExclusiveContent Management Component', () => {
    let comp: ExclusiveContentComponent;
    let fixture: ComponentFixture<ExclusiveContentComponent>;
    let service: ExclusiveContentService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Fun4FundTestModule],
        declarations: [ExclusiveContentComponent],
      })
        .overrideTemplate(ExclusiveContentComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ExclusiveContentComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ExclusiveContentService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new ExclusiveContent(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.exclusiveContents && comp.exclusiveContents[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
