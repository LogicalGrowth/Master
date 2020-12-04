import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ConfigSystemService } from 'app/entities/config-system/config-system.service';
import { IApplicationUser } from 'app/shared/model/application-user.model';
import { ConfigSystem, IConfigSystem } from 'app/shared/model/config-system.model';
import { Observable } from 'rxjs';
import { FeeService } from '../../entities/fee/fee.service';
import { Fee, IFee } from 'app/shared/model/fee.model';
import { AdminPreferencesService } from '../../entities/admin-preferences/admin-preferences.service';
import { AdminPreferences, IAdminPreferences } from '../../shared/model/admin-preferences.model';

@Component({
  selector: 'jhi-bank-account',
  templateUrl: './bank-account.component.html',
  styleUrls: ['../../../content/scss/paper-dashboard.scss', 'bank-account.component.scss'],
})
export class BankAccountComponent implements OnInit {
  regexIbanCrc = /CR[a-zA-Z0-9]{2}\s?([0-9]{4}\s?){4}([0-9]{2})\s?/;
  editForm = this.fb.group({
    id: [],
    type: [],
    value: [null, Validators.compose([Validators.required, Validators.pattern(this.regexIbanCrc)])],
  });

  editFeeForm = this.fb.group({
    id: [],
    highestAmount: [],
    percentage: [null, Validators.compose([Validators.required, Validators.min(0), Validators.pattern('^[0-9]*$'), Validators.max(99)])],
  });

  inactivityForm = this.fb.group({
    id: [],
    inactivityTime: [null, Validators.compose([Validators.required, Validators.min(0), Validators.pattern('^[0-9]*$')])],
  });

  notificationForm = this.fb.group({
    id: [],
    notificationRecurrence: [null, Validators.compose([Validators.required, Validators.min(0), Validators.pattern('^[0-9]*$')])],
  });

  isSaving = false;
  configSystem: ConfigSystem[] = [];
  fee: IFee | null = null;
  adminPreferences: IAdminPreferences | null = null;
  actualConfig: any;

  constructor(
    private fb: FormBuilder,
    protected configSystemService: ConfigSystemService,
    protected feeService: FeeService,
    protected adminPreferencesService: AdminPreferencesService
  ) {}

  ngOnInit(): void {
    this.configSystemService.query({ 'type.equals': 'BankAccount' }).subscribe((res: HttpResponse<IConfigSystem[]>) => {
      this.configSystem = res.body || [];
      if (this.configSystem.length) {
        this.actualConfig = this.configSystem[0];
        this.updateForm(this.actualConfig);
      }
    });

    this.feeService.find(1).subscribe(e => {
      if (e.body) {
        this.fee = e.body;
        this.updateFeeForm(this.fee);
      }
    });

    this.adminPreferencesService.find(1).subscribe(e => {
      if (e.body) {
        this.adminPreferences = e.body;
        this.updateInactivityForm(this.adminPreferences);
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

  updateFeeForm(fee: IFee): void {
    this.editFeeForm.patchValue({
      id: fee.id,
      percentage: fee.percentage,
      highestAmount: fee.highestAmount,
    });
  }

  updateInactivityForm(adminPreferences: IAdminPreferences): void {
    this.inactivityForm.patchValue({
      id: adminPreferences.id,
      inactivityTime: adminPreferences.inactivityTime,
    });
    this.notificationForm.patchValue({
      id: adminPreferences.id,
      notificationRecurrence: adminPreferences.notificationRecurrence,
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

  saveFee(): void {
    this.isSaving = true;
    const fee = this.createFeeFromForm();
    if (fee.id) {
      this.subscribeToSaveResponse(this.feeService.update(fee));
    } else {
      this.subscribeToSaveResponse(this.feeService.create(fee));
    }
  }

  saveInactivity(): void {
    this.isSaving = true;
    const inactivity = this.createInactivityFromForm();
    if (inactivity.id) {
      this.subscribeToSaveResponse(this.adminPreferencesService.update(inactivity));
    } else {
      this.subscribeToSaveResponse(this.adminPreferencesService.create(inactivity));
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

  private createFeeFromForm(): IFee {
    return {
      ...new Fee(),
      id: this.editFeeForm.get(['id'])!.value,
      percentage: this.editFeeForm.get(['percentage'])!.value,
      highestAmount: this.editFeeForm.get(['highestAmount'])!.value,
    };
  }

  private createInactivityFromForm(): IAdminPreferences {
    return {
      ...new AdminPreferences(),
      id: this.inactivityForm.get(['id'])!.value,
      inactivityTime: this.inactivityForm.get(['inactivityTime'])!.value,
      notificationRecurrence: this.notificationForm.get(['notificationRecurrence'])!.value,
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
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IApplicationUser): any {
    return item.id;
  }
}
