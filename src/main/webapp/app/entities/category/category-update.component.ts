import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ICategory, Category } from 'app/shared/model/category.model';
import { CategoryService } from './category.service';
import { IResource, Resource } from 'app/shared/model/resource.model';
import { ResourceService } from 'app/entities/resource/resource.service';

@Component({
  selector: 'jhi-category-update',
  templateUrl: './category-update.component.html',
  styleUrls: ['../../../content/scss/paper-dashboard.scss'],
})
export class CategoryUpdateComponent implements OnInit {
  isSaving = false;
  images: IResource[] = [];
  imageSrc = null;
  resourceToSave: IResource | undefined;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    description: [null, [Validators.required]],
    status: [null, [Validators.required]],
    image: [],
  });

  constructor(
    protected categoryService: CategoryService,
    protected resourceService: ResourceService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ category }) => {
      this.updateForm(category);

      this.resourceService
        .query({ 'categoryId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IResource[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IResource[]) => {
          if (!category.image || !category.image.id) {
            this.images = resBody;
          } else {
            this.resourceService
              .find(category.image.id)
              .pipe(
                map((subRes: HttpResponse<IResource>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IResource[]) => (this.images = concatRes));
          }
        });
    });
  }

  updateForm(category: ICategory): void {
    this.editForm.patchValue({
      id: category.id,
      name: category.name,
      description: category.description,
      status: category.status,
      image: category.image,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const category = this.createFromForm();
    if (category.id !== undefined) {
      this.subscribeToSaveResponse(this.categoryService.update(category));
    } else {
      this.subscribeToSaveResponse(this.categoryService.create(category));
    }
  }

  private createFromForm(): ICategory {
    return {
      ...new Category(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      status: this.editForm.get(['status'])!.value,
      image: this.resourceToSave ?? this.editForm.get(['image_id'])!.value,
    };
  }

  private createFromFormResource(url = '', type = ''): IResource {
    return {
      ...new Resource(),
      url: url ? url : this.editForm.get(['url'])!.value,
      type: type ? type : this.editForm.get(['type'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICategory>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected subscribeToSaveResponseResource(result: Observable<HttpResponse<IResource>>): void {
    result.subscribe(resp => {
      this.resourceToSave = resp.body ? resp.body : undefined;
    });
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IResource): any {
    return item.id;
  }

  saveImage(data: any): void {
    const image = this.createFromFormResource(data.secure_url, 'Image');
    this.subscribeToSaveResponseResource(this.resourceService.create(image));
    this.imageSrc = data.secure_url;
  }
}
