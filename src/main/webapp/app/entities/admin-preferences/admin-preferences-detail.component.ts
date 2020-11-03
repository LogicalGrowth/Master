import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAdminPreferences } from 'app/shared/model/admin-preferences.model';

@Component({
  selector: 'jhi-admin-preferences-detail',
  templateUrl: './admin-preferences-detail.component.html',
})
export class AdminPreferencesDetailComponent implements OnInit {
  adminPreferences: IAdminPreferences | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ adminPreferences }) => (this.adminPreferences = adminPreferences));
  }

  previousState(): void {
    window.history.back();
  }
}
