import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { Fun4FundTestModule } from '../../../test.module';
import { CheckpointComponent } from 'app/entities/checkpoint/checkpoint.component';
import { CheckpointService } from 'app/entities/checkpoint/checkpoint.service';
import { Checkpoint } from 'app/shared/model/checkpoint.model';

describe('Component Tests', () => {
  describe('Checkpoint Management Component', () => {
    let comp: CheckpointComponent;
    let fixture: ComponentFixture<CheckpointComponent>;
    let service: CheckpointService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Fun4FundTestModule],
        declarations: [CheckpointComponent],
      })
        .overrideTemplate(CheckpointComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CheckpointComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CheckpointService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Checkpoint(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.checkpoints && comp.checkpoints[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
