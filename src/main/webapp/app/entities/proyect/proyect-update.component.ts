import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { AbstractControl, FormBuilder, FormGroup, ValidatorFn, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';

import { IProyect, Proyect } from 'app/shared/model/proyect.model';
import { ProyectService } from './proyect.service';
import { IApplicationUser } from 'app/shared/model/application-user.model';
import { ApplicationUserService } from 'app/entities/application-user/application-user.service';
import { ICategory } from 'app/shared/model/category.model';
import { CategoryService } from 'app/entities/category/category.service';
import { FeeService } from '../fee/fee.service';
import { IFee } from 'app/shared/model/fee.model';
import { CategoryStatus } from 'app/shared/model/enumerations/category-status.model';

type SelectableEntity = IApplicationUser | ICategory;

const MyAwesomeRangeValidator: ValidatorFn = (fg: any) => {
  const start = fg.get('collected').value || 0;
  const end = fg.get('goalAmount').value || 0;
  return start !== null && end !== null && start < end ? null : { range: true };
};

@Component({
  selector: 'jhi-proyect-update',
  templateUrl: './proyect-update.component.html',
  styleUrls: ['../../../content/scss/paper-dashboard.scss'],
})
export class ProyectUpdateComponent implements OnInit {
  isSaving = false;
  regexIbanCrc = /CR[a-zA-Z0-9]{2}\s?([0-9]{4}\s?){4}([0-9]{2})\s?/;
  applicationusers: IApplicationUser[] = [];
  categories: ICategory[] = [];
  fee: IFee[] = [];
  hasMarker = false;
  position: any;
  updateProyect: any;
  isUpdate = false;
  proyectFee: any;
  goal: any;
  collected: any;

  editForm = this.fb.group(
    {
      id: [],
      name: [null, [Validators.required, Validators.maxLength(30)]],
      description: [null, [Validators.required, Validators.maxLength(300)]],
      idType: [null, [Validators.required]],
      goalAmount: [null, [Validators.required, Validators.min(1), Validators.pattern('^[0-9]*$')]],
      coordX: [[Validators.required]],
      coordY: [],
      fee: [],
      collected: [],
      number: [null, Validators.compose([Validators.required, Validators.pattern(this.regexIbanCrc)])],
      currencyType: [null, [Validators.required]],
      category: [null, [Validators.required]],
    },
    { validator: MyAwesomeRangeValidator }
  );

  constructor(
    protected proyectService: ProyectService,
    protected applicationUserService: ApplicationUserService,
    protected categoryService: CategoryService,
    protected feeService: FeeService,
    protected activatedRoute: ActivatedRoute,
    private router: Router,
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
      this.updateProyect = proyect;

      this.applicationUserService.query().subscribe((res: HttpResponse<IApplicationUser[]>) => (this.applicationusers = res.body || []));

      this.categoryService
        .query({ 'status.equals': CategoryStatus.ENABLED })
        .subscribe((res: HttpResponse<ICategory[]>) => (this.categories = res.body || []));

      this.feeService.query({ page: 1, size: 1 }).subscribe((res: HttpResponse<IFee[]>) => {
        this.fee = res.body || [];
        if (!proyect.id) {
          this.proyectFee = this.fee[0].percentage;
        } else {
          this.proyectFee = proyect.fee;
        }
      });
    });
  }

  updateForm(proyect: IProyect): void {
    this.editForm.patchValue({
      id: proyect.id,
      name: proyect.name,
      description: proyect.description,
      idType: proyect.idType,
      goalAmount: proyect.goalAmount,
      fee: 0,
      collected: proyect.collected,
      number: proyect.number,
      currencyType: proyect.currencyType,
      category: proyect.category,
    });
    this.goal = proyect.goalAmount;
    this.collected = proyect.collected;
    this.position = {
      lat: proyect.coordY,
      lng: proyect.coordX,
    };
    this.hasMarker = true;
  }

  goToAddImage(data: any): void {
    this.router.navigate(['/proyect/' + data.body.id + '/image/new']);
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const proyect = this.createFromForm();
    if (proyect.id !== undefined) {
      proyect.collected = this.updateProyect.collected;
      proyect.rating = this.updateProyect.rating;
      proyect.owner = this.updateProyect.owner;
      proyect.creationDate = this.updateProyect.creationDate;
      proyect.fee = this.updateProyect.fee;
      this.isUpdate = true;
      this.subscribeToSaveResponse(this.proyectService.update(proyect));
    } else {
      proyect.collected = 0;
      proyect.rating = 0;
      proyect.creationDate = moment();
      proyect.fee = this.fee[0].percentage;
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
      lastUpdated: moment(),
      coordX: this.position.lng,
      coordY: this.position.lat || 0,
      fee: 0,
      number: this.editForm.get(['number'])!.value,
      currencyType: this.editForm.get(['currencyType'])!.value,
      category: this.editForm.get(['category'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProyect>>): void {
    result.subscribe(
      (data: any) => this.onSaveSuccess(data),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(data: any): void {
    this.isSaving = false;
    if (!this.isUpdate) {
      this.goToAddImage(data);
      return;
    }
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }

  onMapClick($event: any): void {
    this.position = {
      lat: $event.latLng.lat(),
      lng: $event.latLng.lng(),
    };
    this.hasMarker = true;
  }
}
