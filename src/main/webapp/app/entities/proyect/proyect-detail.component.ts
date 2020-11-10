import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IExclusiveContent } from 'app/shared/model/exclusive-content.model';

import { IProyect } from 'app/shared/model/proyect.model';
import { ExclusiveContentService } from '../exclusive-content/exclusive-content.service';
import { AccountService } from 'app/core/auth/account.service';
import { User } from 'app/core/user/user.model';
import { ActivityStatus } from 'app/shared/model/enumerations/activity-status.model';

@Component({
  selector: 'jhi-proyect-detail',
  templateUrl: './proyect-detail.component.html',
  styleUrls: ['../../../content/scss/paper-dashboard.scss'],
})
export class ProyectDetailComponent implements OnInit {
  proyect: IProyect | null = null;
  exclusiveContents?: IExclusiveContent[];
  account!: User;
  isProjectOwner!: Boolean;

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected exclusiveContentService: ExclusiveContentService,
    private accountService: AccountService
  ) {}

  loadExclusiveContent(projectId: number): void {
    if (this.proyect != null) {
      if (this.isProjectOwner) {
        this.exclusiveContentService
          .query({ 'proyectId.equals': projectId })
          .subscribe((res: HttpResponse<IExclusiveContent[]>) => (this.exclusiveContents = res.body || []));
      } else {
        this.exclusiveContentService
          .query({ 'proyectId.equals': projectId, 'stock.greaterThan': 1, 'state.equals': ActivityStatus.ENABLED })
          .subscribe((res: HttpResponse<IExclusiveContent[]>) => (this.exclusiveContents = res.body || []));
      }
    }
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ proyect }) => (this.proyect = proyect));

    this.accountService.identity().subscribe(account => {
      if (account) {
        this.account = account;
      }
    });

    this.isProjectOwner = this.account.id === this.proyect?.owner?.id ? true : false;

    this.loadExclusiveContent(this.proyect?.id as number);
  }

  previousState(): void {
    window.history.back();
  }
}
