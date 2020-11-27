import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IConfigSystem } from 'app/shared/model/config-system.model';
import { ConfigSystemService } from './config-system.service';
import { ConfigSystemDeleteDialogComponent } from './config-system-delete-dialog.component';

@Component({
  selector: 'jhi-config-system',
  templateUrl: './config-system.component.html',
})
export class ConfigSystemComponent implements OnInit, OnDestroy {
  configSystems?: IConfigSystem[];
  eventSubscriber?: Subscription;

  constructor(
    protected configSystemService: ConfigSystemService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.configSystemService.query().subscribe((res: HttpResponse<IConfigSystem[]>) => (this.configSystems = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInConfigSystems();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IConfigSystem): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInConfigSystems(): void {
    this.eventSubscriber = this.eventManager.subscribe('configSystemListModification', () => this.loadAll());
  }

  delete(configSystem: IConfigSystem): void {
    const modalRef = this.modalService.open(ConfigSystemDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.configSystem = configSystem;
  }
}
