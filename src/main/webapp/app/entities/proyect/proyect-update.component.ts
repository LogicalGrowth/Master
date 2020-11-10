import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IProyect, Proyect } from 'app/shared/model/proyect.model';
import { ProyectService } from './proyect.service';
import { IApplicationUser } from 'app/shared/model/application-user.model';
import { ApplicationUserService } from 'app/entities/application-user/application-user.service';
import { ICategory } from 'app/shared/model/category.model';
import { CategoryService } from 'app/entities/category/category.service';

type SelectableEntity = IApplicationUser | ICategory;

@Component({
  selector: 'jhi-proyect-update',
  templateUrl: './proyect-update.component.html',
})
export class ProyectUpdateComponent implements OnInit {
  isSaving = false;
  applicationusers: IApplicationUser[] = [];
  categories: ICategory[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.maxLength(30)]],
    description: [null, [Validators.required, Validators.maxLength(300)]],
    idType: [null, [Validators.required]],
    goalAmount: [null, [Validators.required, Validators.min(1)]],
    collected: [null, [Validators.min(0)]],
    rating: [],
    creationDate: [null, [Validators.required]],
    lastUpdated: [],
    coordX: [null, [Validators.required]],
    coordY: [null, [Validators.required]],
    fee: [null, [Validators.required]],
    number: [null, [Validators.required]],
    currencyType: [null, [Validators.required]],
    owner: [],
    applicationUser: [],
    category: [],
  });

  constructor(
    protected proyectService: ProyectService,
    protected applicationUserService: ApplicationUserService,
    protected categoryService: CategoryService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ proyect }) => {
      if (!proyect.id) {
        const today = moment().startOf('day');
        proyect.creationDate = today;
        proyect.lastUpdated = today;
      }

      this.updateForm(proyect);

      this.applicationUserService.query().subscribe((res: HttpResponse<IApplicationUser[]>) => (this.applicationusers = res.body || []));

      this.categoryService.query().subscribe((res: HttpResponse<ICategory[]>) => (this.categories = res.body || []));
    });
  }

  updateForm(proyect: IProyect): void {
    this.editForm.patchValue({
      id: proyect.id,
      name: proyect.name,
      description: proyect.description,
      idType: proyect.idType,
      goalAmount: proyect.goalAmount,
      collected: proyect.collected,
      rating: proyect.rating,
      creationDate: proyect.creationDate ? proyect.creationDate.format(DATE_TIME_FORMAT) : null,
      lastUpdated: proyect.lastUpdated ? proyect.lastUpdated.format(DATE_TIME_FORMAT) : null,
      coordX: proyect.coordX,
      coordY: proyect.coordY,
      fee: proyect.fee,
      number: proyect.number,
      currencyType: proyect.currencyType,
      owner: proyect.owner,
      applicationUser: proyect.applicationUser,
      category: proyect.category,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const proyect = this.createFromForm();
    if (proyect.id !== undefined) {
      this.subscribeToSaveResponse(this.proyectService.update(proyect));
    } else {
      this.subscribeToSaveResponse(this.proyectService.create(proyect));
    }
  }

  private createFromForm(): IProyect {
    return {
      ...new Proyect(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      idType: this.editForm.get(['idType'])!.value,
      goalAmount: this.editForm.get(['goalAmount'])!.value,
      collected: this.editForm.get(['collected'])!.value,
      rating: this.editForm.get(['rating'])!.value,
      creationDate: this.editForm.get(['creationDate'])!.value
        ? moment(this.editForm.get(['creationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      lastUpdated: this.editForm.get(['lastUpdated'])!.value
        ? moment(this.editForm.get(['lastUpdated'])!.value, DATE_TIME_FORMAT)
        : undefined,
      coordX: this.editForm.get(['coordX'])!.value,
      coordY: this.editForm.get(['coordY'])!.value,
      fee: this.editForm.get(['fee'])!.value,
      number: this.editForm.get(['number'])!.value,
      currencyType: this.editForm.get(['currencyType'])!.value,
      owner: this.editForm.get(['owner'])!.value,
      applicationUser: this.editForm.get(['applicationUser'])!.value,
      category: this.editForm.get(['category'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProyect>>): void {
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
