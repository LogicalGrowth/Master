import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IExclusiveContent, ExclusiveContent } from 'app/shared/model/exclusive-content.model';
import { ExclusiveContentService } from './exclusive-content.service';
import { ResourceService } from 'app/entities//resource/resource.service';
import { IPrize, Prize } from 'app/shared/model/prize.model';
import { PrizeService } from 'app/entities/prize/prize.service';
import { IProyect } from 'app/shared/model/proyect.model';
import { ProyectService } from 'app/entities/proyect/proyect.service';
import { IResource, Resource } from 'app/shared/model/resource.model';
import { ActivityStatus } from 'app/shared/model/enumerations/activity-status.model';

type SelectableEntity = IPrize | IProyect;

@Component({
  selector: 'jhi-exclusive-content-update',
  templateUrl: './exclusive-content-update.component.html',
  styleUrls: ['../../../content/scss/paper-dashboard.scss'],
})
export class ExclusiveContentUpdateComponent implements OnInit {
  creating = true;
  isSaving = false;
  prizes: IPrize[] = [];
  proyect?: IProyect;
  images: IResource[] = [];
  prize?: IPrize | null;
  imageSrc = '';

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    description: [null, [Validators.required]],
    price: [null, [Validators.required, Validators.min(0)]],
    stock: [null, [Validators.required, Validators.min(0)]],
    state: [],
    prize: [],
    proyect: [],
  });

  constructor(
    protected exclusiveContentService: ExclusiveContentService,
    protected resourceService: ResourceService,
    protected prizeService: PrizeService,
    protected proyectService: ProyectService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  getImages(exclusiveContentId: number): any {
    if (exclusiveContentId)
      this.resourceService
        .query({ 'prizeId.equals': exclusiveContentId })
        .pipe(
          map((res: HttpResponse<IResource[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IResource[]) => {
          this.images = resBody;
        });
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ exclusiveContent }) => {
      if (exclusiveContent) {
        this.creating = false;
        this.updateForm(exclusiveContent);
        this.prize = exclusiveContent.prize;
        this.proyect = exclusiveContent.proyect;

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

    this.getImages(this.prize?.id as number);
  }

  updateForm(exclusiveContent: IExclusiveContent): void {
    this.editForm.patchValue({
      id: exclusiveContent.id,
      image: exclusiveContent.prize?.images,
      name: exclusiveContent.prize?.name,
      description: exclusiveContent.prize?.description,
      price: exclusiveContent.price,
      stock: exclusiveContent.stock,
      state: exclusiveContent.state,
      prize: exclusiveContent.prize,
      proyect: exclusiveContent.proyect?.id,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    let exclusiveContent: IExclusiveContent;
    if (this.creating) {
      exclusiveContent = this.createFromForm();
    } else {
      exclusiveContent = this.updateFromForm();
    }
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
      images: this.images,
    };

    return {
      ...new ExclusiveContent(),
      id: this.editForm.get(['id'])!.value,
      price: this.editForm.get(['price'])!.value,
      stock: this.editForm.get(['stock'])!.value,
      state: ActivityStatus.ENABLED,
      prize: newPrize,
      proyect: this.editForm.get(['proyect'])!.value,
    };
  }

  private updateFromForm(): IExclusiveContent {
    const newPrize = {
      ...new Prize(),
      id: this.prize?.id,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      images: this.images,
    };

    return {
      ...new ExclusiveContent(),
      id: this.editForm.get(['id'])!.value,
      price: this.editForm.get(['price'])!.value,
      stock: this.editForm.get(['stock'])!.value,
      state: this.editForm.get(['state'])!.value,
      prize: newPrize,
      proyect: this.proyect!,
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

  saveImage(data: any): void {
    this.imageSrc = data.secure_url;
    const newResource = {
      ...new Resource(),
      id: undefined,
      url: data.secure_url,
      type: 'Image',
      proyect: undefined,
      prize: undefined,
    };

    this.images?.push(newResource);
  }
}
