import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { Fun4FundTestModule } from '../../../test.module';
import { FavoriteComponent } from 'app/entities/favorite/favorite.component';
import { FavoriteService } from 'app/entities/favorite/favorite.service';
import { Favorite } from 'app/shared/model/favorite.model';

describe('Component Tests', () => {
  describe('Favorite Management Component', () => {
    let comp: FavoriteComponent;
    let fixture: ComponentFixture<FavoriteComponent>;
    let service: FavoriteService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Fun4FundTestModule],
        declarations: [FavoriteComponent],
      })
        .overrideTemplate(FavoriteComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FavoriteComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FavoriteService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Favorite(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.favorites && comp.favorites[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
