import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IProyectAccount, ProyectAccount } from 'app/shared/model/proyect-account.model';
import { ProyectAccountService } from './proyect-account.service';

@Component({
  selector: 'jhi-proyect-account-update',
  templateUrl: './proyect-account-update.component.html',
})
export class ProyectAccountUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    number: [null, [Validators.required]],
    currencyType: [null, [Validators.required]],
  });

  constructor(protected proyectAccountService: ProyectAccountService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ proyectAccount }) => {
      this.updateForm(proyectAccount);
    });
  }

  updateForm(proyectAccount: IProyectAccount): void {
    this.editForm.patchValue({
      id: proyectAccount.id,
      number: proyectAccount.number,
      currencyType: proyectAccount.currencyType,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const proyectAccount = this.createFromForm();
    if (proyectAccount.id !== undefined) {
      this.subscribeToSaveResponse(this.proyectAccountService.update(proyectAccount));
    } else {
      this.subscribeToSaveResponse(this.proyectAccountService.create(proyectAccount));
    }
  }

  private createFromForm(): IProyectAccount {
    return {
      ...new ProyectAccount(),
      id: this.editForm.get(['id'])!.value,
      number: this.editForm.get(['number'])!.value,
      currencyType: this.editForm.get(['currencyType'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProyectAccount>>): void {
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
}
