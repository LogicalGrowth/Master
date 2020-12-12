import { HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ExclusiveContentService } from 'app/entities/exclusive-content/exclusive-content.service';
import { Observable } from 'rxjs';
import { ExclusiveContent, IExclusiveContent } from '../../../shared/model/exclusive-content.model';
import { ExclusiveContentBuyoutComponent } from './exclusive-content-buyout.component';

@Injectable({
  providedIn: 'root',
})
export class ExclusiveContentBuyoutService {
  private isOpen = false;
  private exclusiveContent: ExclusiveContent | undefined;

  constructor(private modalService: NgbModal, private exclusiveContentService: ExclusiveContentService) {}

  open(exclusiveContent: IExclusiveContent): void {
    this.exclusiveContent = exclusiveContent;
    if (this.isOpen) {
      return;
    }
    this.isOpen = true;
    const modalRef: NgbModalRef = this.modalService.open(ExclusiveContentBuyoutComponent);
    modalRef.componentInstance.exclusiveContent = exclusiveContent;
    modalRef.result.finally(() => (this.isOpen = false));
  }

  updateExclusiveContent(): void {
    this.exclusiveContent!.stock = (this.exclusiveContent!.stock as number) - 1;
    this.subscribeToSaveResponse(this.exclusiveContentService.update(this.exclusiveContent!));
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExclusiveContent>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {}

  protected onSaveError(): void {}

  close(): void {
    this.isOpen = false;
    this.modalService.dismissAll();

    setTimeout(() => {
      location.reload();
    }, 2000);
  }
}
