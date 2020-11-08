import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { IProyect, Proyect } from 'app/shared/model/proyect.model';
import { ProyectService } from './proyect.service';
import { IProyectAccount } from 'app/shared/model/proyect-account.model';
import { ProyectAccountService } from 'app/entities/proyect-account/proyect-account.service';
import { IApplicationUser } from 'app/shared/model/application-user.model';
import { ApplicationUserService } from 'app/entities/application-user/application-user.service';
import { ICategory } from 'app/shared/model/category.model';
import { CategoryService } from 'app/entities/category/category.service';

type SelectableEntity = IProyectAccount | IApplicationUser | ICategory;

@Component({
  selector: 'jhi-proyect-update',
  templateUrl: './proyect-update.component.html',
  styleUrls: ['../../../content/scss/paper-dashboard.scss'],
})
export class ProyectUpdateComponent implements OnInit {
  isSaving = false;
  accounts: IProyectAccount[] = [];
  applicationusers: IApplicationUser[] = [];
  categories: ICategory[] = [];
  hasMarker = false;
  position: any[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.maxLength(30)]],
    description: [null, [Validators.required, Validators.maxLength(300)]],
    idType: [null, [Validators.required]],
    goalAmount: [null, [Validators.required, Validators.min(1)]],
    coordX: [[Validators.required]],
    coordY: [],
    fee: [],
    account: [null, [Validators.required]],
    category: [null, [Validators.required]],
  });

  constructor(
    protected proyectService: ProyectService,
    protected proyectAccountService: ProyectAccountService,
    protected applicationUserService: ApplicationUserService,
    protected categoryService: CategoryService,
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

      this.proyectAccountService
        .query({ filter: 'proyect-is-null' })
        .pipe(
          map((res: HttpResponse<IProyectAccount[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IProyectAccount[]) => {
          if (!proyect.account || !proyect.account.id) {
            this.accounts = resBody;
          } else {
            this.proyectAccountService
              .find(proyect.account.id)
              .pipe(
                map((subRes: HttpResponse<IProyectAccount>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IProyectAccount[]) => (this.accounts = concatRes));
          }
        });

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
      coordX: this.position[1],
      coordY: this.position[0],
      fee: 0,
      account: proyect.account,
      category: proyect.category,
    });
  }

  goToAddImage(data: any): void {
    window.sessionStorage.proyect = data.body.id;
    this.router.navigate(['/proyect/image/new']);
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
      creationDate: moment(),
      coordX: this.position[1],
      coordY: this.position[0] || 0,
      fee: 0,
      account: this.editForm.get(['account'])!.value,
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
    this.goToAddImage(data);
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }

  onMapClick($event: any): void {
    this.position.push($event.latLng.lat());
    this.position.push($event.latLng.lng());
    this.hasMarker = true;
  }
}
