import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IExclusiveContent, ExclusiveContent } from 'app/shared/model/exclusive-content.model';
import { ExclusiveContentService } from './exclusive-content.service';
import { IPrize, Prize } from 'app/shared/model/prize.model';
import { PrizeService } from 'app/entities/prize/prize.service';
import { IProyect } from 'app/shared/model/proyect.model';
import { ProyectService } from 'app/entities/proyect/proyect.service';
import { Resource } from 'app/shared/model/resource.model';

type SelectableEntity = IPrize | IProyect;

@Component({
  selector: 'jhi-exclusive-content-update',
  templateUrl: './exclusive-content-update.component.html',
  styleUrls: ['../../../content/scss/paper-dashboard.scss'],
})
export class ExclusiveContentUpdateComponent implements OnInit {
  isSaving = false;
  prizes: IPrize[] = [];
  proyects: IProyect[] = [];
  proyect?: IProyect | null;

  editForm = this.fb.group(
    {
      id: [],
      image: [null, [Validators.required]],
      name: [null, [Validators.required]],
      description: [null, [Validators.required]],
      price: [null, [Validators.required, Validators.min(0)]],
      stock: [null, [Validators.min(0)]],
      state: [null, [Validators.required]],
      prize: [],
      proyect: [],
    },
    { updateOn: 'submit' }
  );

  constructor(
    protected exclusiveContentService: ExclusiveContentService,
    protected prizeService: PrizeService,
    protected proyectService: ProyectService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ exclusiveContent }) => {
      if (exclusiveContent) {
        this.updateForm(exclusiveContent);

        this.prizeService
          .query({ 'exclusiveContentId.specified': 'false' })
          .pipe(
            map((res: HttpResponse<IPrize[]>) => {
              return res.body || [];
            })
          )
          .subscribe((resBody: IPrize[]) => {
            if (!exclusiveContent.prize || !exclusiveContent.prize.id) {
              this.prizes = resBody;
            } else {
              this.prizeService
                .find(exclusiveContent.prize.id)
                .pipe(
                  map((subRes: HttpResponse<IPrize>) => {
                    return subRes.body ? [subRes.body].concat(resBody) : resBody;
                  })
                )
                .subscribe((concatRes: IPrize[]) => (this.prizes = concatRes));
            }
          });

        this.proyectService.query().subscribe((res: HttpResponse<IProyect[]>) => (this.proyects = res.body || []));
      }
    });

    this.activatedRoute.data.subscribe(({ currentProyect }) => {
      if (currentProyect) {
        this.editForm.patchValue({
          id: undefined,
          proyect: currentProyect,
        });
      }
    });
  }

  updateForm(exclusiveContent: IExclusiveContent): void {
    this.editForm.patchValue({
      id: exclusiveContent.id,
      price: exclusiveContent.price,
      stock: exclusiveContent.stock,
      state: exclusiveContent.state,
      prize: exclusiveContent.prize,
      proyect: exclusiveContent.proyect,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const exclusiveContent = this.createFromForm();
    if (exclusiveContent.id !== undefined) {
      this.subscribeToSaveResponse(this.exclusiveContentService.update(exclusiveContent));
    } else {
      this.subscribeToSaveResponse(this.exclusiveContentService.create(exclusiveContent));
    }
  }

  private createFromForm(): IExclusiveContent {
    const newPrize = {
      ...new Prize(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      images: [
        {
          ...new Resource(),
          id: this.editForm.get(['id'])!.value,
          url: this.editForm.get(['image'])!.value,
          type: 'image',
        },
      ],
    };

    return {
      ...new ExclusiveContent(),
      id: this.editForm.get(['id'])!.value,
      price: this.editForm.get(['price'])!.value,
      stock: this.editForm.get(['stock'])!.value,
      state: this.editForm.get(['state'])!.value,
      prize: newPrize,
      proyect: this.editForm.get(['proyect'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExclusiveContent>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
