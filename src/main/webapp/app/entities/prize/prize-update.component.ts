import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IPrize, Prize } from 'app/shared/model/prize.model';
import { PrizeService } from './prize.service';

@Component({
  selector: 'jhi-prize-update',
  templateUrl: './prize-update.component.html',
})
export class PrizeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    description: [null, [Validators.required]],
  });

  constructor(protected prizeService: PrizeService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ prize }) => {
      this.updateForm(prize);
    });
  }

  updateForm(prize: IPrize): void {
    this.editForm.patchValue({
      id: prize.id,
      name: prize.name,
      description: prize.description,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const prize = this.createFromForm();
    if (prize.id !== undefined) {
      this.subscribeToSaveResponse(this.prizeService.update(prize));
    } else {
      this.subscribeToSaveResponse(this.prizeService.create(prize));
    }
  }

  private createFromForm(): IPrize {
    return {
      ...new Prize(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPrize>>): void {
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
