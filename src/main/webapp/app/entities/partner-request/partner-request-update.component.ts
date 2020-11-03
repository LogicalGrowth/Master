import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IPartnerRequest, PartnerRequest } from 'app/shared/model/partner-request.model';
import { PartnerRequestService } from './partner-request.service';
import { IApplicationUser } from 'app/shared/model/application-user.model';
import { ApplicationUserService } from 'app/entities/application-user/application-user.service';
import { IProyect } from 'app/shared/model/proyect.model';
import { ProyectService } from 'app/entities/proyect/proyect.service';

type SelectableEntity = IApplicationUser | IProyect;

@Component({
  selector: 'jhi-partner-request-update',
  templateUrl: './partner-request-update.component.html',
})
export class PartnerRequestUpdateComponent implements OnInit {
  isSaving = false;
  applicants: IApplicationUser[] = [];
  proyects: IProyect[] = [];

  editForm = this.fb.group({
    id: [],
    amount: [null, [Validators.required, Validators.min(0)]],
    status: [null, [Validators.required]],
    applicant: [],
    proyect: [],
  });

  constructor(
    protected partnerRequestService: PartnerRequestService,
    protected applicationUserService: ApplicationUserService,
    protected proyectService: ProyectService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ partnerRequest }) => {
      this.updateForm(partnerRequest);

      this.applicationUserService
        .query({ filter: 'partnerrequest-is-null' })
        .pipe(
          map((res: HttpResponse<IApplicationUser[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IApplicationUser[]) => {
          if (!partnerRequest.applicant || !partnerRequest.applicant.id) {
            this.applicants = resBody;
          } else {
            this.applicationUserService
              .find(partnerRequest.applicant.id)
              .pipe(
                map((subRes: HttpResponse<IApplicationUser>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IApplicationUser[]) => (this.applicants = concatRes));
          }
        });

      this.proyectService.query().subscribe((res: HttpResponse<IProyect[]>) => (this.proyects = res.body || []));
    });
  }

  updateForm(partnerRequest: IPartnerRequest): void {
    this.editForm.patchValue({
      id: partnerRequest.id,
      amount: partnerRequest.amount,
      status: partnerRequest.status,
      applicant: partnerRequest.applicant,
      proyect: partnerRequest.proyect,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const partnerRequest = this.createFromForm();
    if (partnerRequest.id !== undefined) {
      this.subscribeToSaveResponse(this.partnerRequestService.update(partnerRequest));
    } else {
      this.subscribeToSaveResponse(this.partnerRequestService.create(partnerRequest));
    }
  }

  private createFromForm(): IPartnerRequest {
    return {
      ...new PartnerRequest(),
      id: this.editForm.get(['id'])!.value,
      amount: this.editForm.get(['amount'])!.value,
      status: this.editForm.get(['status'])!.value,
      applicant: this.editForm.get(['applicant'])!.value,
      proyect: this.editForm.get(['proyect'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPartnerRequest>>): void {
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
