import { HttpResponse } from '@angular/common/http';
import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { PartnerRequestService } from 'app/entities/partner-request/partner-request.service';
import { RequestStatus } from 'app/shared/model/enumerations/request-status.model';
import { IPartnerRequest, PartnerRequest } from 'app/shared/model/partner-request.model';
import { IProyect } from 'app/shared/model/proyect.model';
import { Observable } from 'rxjs';

@Component({
  selector: 'jhi-partner-request',
  templateUrl: './partner-request.component.html',
  styleUrls: ['../../../../content/scss/paper-dashboard.scss'],
})
export class PartnerRequestComponent implements OnInit {
  @Input() public proyect: IProyect | undefined;
  isSaving = false;
  editForm = this.fb.group({
    id: [],
    amount: [null, [Validators.required, Validators.pattern('^[0-9]*$')]],
  });

  constructor(private fb: FormBuilder, public activeModal: NgbActiveModal, private partnerRequestService: PartnerRequestService) {}

  ngOnInit(): void {}

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
      status: RequestStatus.SEND,
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
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
