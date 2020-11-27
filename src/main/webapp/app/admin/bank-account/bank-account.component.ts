import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ConfigSystemService } from 'app/entities/config-system/config-system.service';
import { IApplicationUser } from 'app/shared/model/application-user.model';
import { ConfigSystem, IConfigSystem } from 'app/shared/model/config-system.model';
import { Observable } from 'rxjs';

@Component({
  selector: 'jhi-bank-account',
  templateUrl: './bank-account.component.html',
  styleUrls: ['../../../content/scss/paper-dashboard.scss'],
})
export class BankAccountComponent implements OnInit {
  regexIbanCrc = /CR[a-zA-Z0-9]{2}\s?([0-9]{4}\s?){4}([0-9]{2})\s?/;
  editForm = this.fb.group({
    id: [],
    type: [],
    value: [null, Validators.compose([Validators.required, Validators.pattern(this.regexIbanCrc)])],
  });
  isSaving = false;
  configSystem: ConfigSystem[] = [];
  actualConfig: any;

  constructor(private fb: FormBuilder, protected configSystemService: ConfigSystemService) {}

  ngOnInit(): void {
    this.configSystemService.query({ 'type.equals': 'BankAccount' }).subscribe((res: HttpResponse<IConfigSystem[]>) => {
      this.configSystem = res.body || [];
      if (this.configSystem.length) {
        this.actualConfig = this.configSystem[0];
        this.updateForm(this.actualConfig);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  updateForm(configSystem: IConfigSystem): void {
    this.editForm.patchValue({
      id: configSystem.id,
      type: configSystem.type,
      value: configSystem.value,
    });
  }

  save(): void {
    this.isSaving = true;
    const config = this.createFromForm();
    if (config.id) {
      this.subscribeToSaveResponse(this.configSystemService.update(config));
    } else {
      this.subscribeToSaveResponse(this.configSystemService.create(config));
    }
  }

  private createFromForm(): IConfigSystem {
    return {
      ...new ConfigSystem(),
      id: this.editForm.get(['id'])!.value,
      type: 'BankAccount',
      value: this.editForm.get(['value'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IConfigSystem>>): void {
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

  trackById(index: number, item: IApplicationUser): any {
    return item.id;
  }
}
