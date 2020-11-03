import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IProyect } from 'app/shared/model/proyect.model';
import { ProyectService } from './proyect.service';
import { ProyectDeleteDialogComponent } from './proyect-delete-dialog.component';

@Component({
  selector: 'jhi-proyect',
  templateUrl: './proyect.component.html',
})
export class ProyectComponent implements OnInit, OnDestroy {
  proyects?: IProyect[];
  eventSubscriber?: Subscription;

  constructor(protected proyectService: ProyectService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.proyectService.query().subscribe((res: HttpResponse<IProyect[]>) => (this.proyects = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInProyects();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IProyect): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInProyects(): void {
    this.eventSubscriber = this.eventManager.subscribe('proyectListModification', () => this.loadAll());
  }

  delete(proyect: IProyect): void {
    const modalRef = this.modalService.open(ProyectDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.proyect = proyect;
  }
}
