import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { Fun4FundTestModule } from '../../../test.module';
import { CheckpointDetailComponent } from 'app/entities/checkpoint/checkpoint-detail.component';
import { Checkpoint } from 'app/shared/model/checkpoint.model';

describe('Component Tests', () => {
  describe('Checkpoint Management Detail Component', () => {
    let comp: CheckpointDetailComponent;
    let fixture: ComponentFixture<CheckpointDetailComponent>;
    const route = ({ data: of({ checkpoint: new Checkpoint(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Fun4FundTestModule],
        declarations: [CheckpointDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(CheckpointDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CheckpointDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load checkpoint on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.checkpoint).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
