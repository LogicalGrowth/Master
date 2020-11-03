import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IProyectAccount } from 'app/shared/model/proyect-account.model';
import { ProyectAccountService } from './proyect-account.service';
import { ProyectAccountDeleteDialogComponent } from './proyect-account-delete-dialog.component';

@Component({
  selector: 'jhi-proyect-account',
  templateUrl: './proyect-account.component.html',
})
export class ProyectAccountComponent implements OnInit, OnDestroy {
  proyectAccounts?: IProyectAccount[];
  eventSubscriber?: Subscription;

  constructor(
    protected proyectAccountService: ProyectAccountService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.proyectAccountService.query().subscribe((res: HttpResponse<IProyectAccount[]>) => (this.proyectAccounts = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInProyectAccounts();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IProyectAccount): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInProyectAccounts(): void {
    this.eventSubscriber = this.eventManager.subscribe('proyectAccountListModification', () => this.loadAll());
  }

  delete(proyectAccount: IProyectAccount): void {
    const modalRef = this.modalService.open(ProyectAccountDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.proyectAccount = proyectAccount;
  }
}
