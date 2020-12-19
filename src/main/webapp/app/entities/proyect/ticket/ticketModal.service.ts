import { Injectable } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { TicketComponent } from './ticket.component';
import { IProyect } from 'app/shared/model/proyect.model';
import { IRaffle } from 'app/shared/model/raffle.model';
import { TicketService } from 'app/entities/ticket/ticket.service';
import { Ticket, ITicket } from 'app/shared/model/ticket.model';
import { HttpResponse } from '@angular/common/http';
import { IApplicationUser } from 'app/shared/model/application-user.model';
import { AccountService } from 'app/core/auth/account.service';
import { ApplicationUserService } from 'app/entities/application-user/application-user.service';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class TicketModalService {
  private isOpen = false;
  private globalRaffle: any;
  applicationUser: IApplicationUser[] | undefined;
  account: any;
  constructor(
    private modalService: NgbModal,
    private ticketService: TicketService,
    private accountService: AccountService,
    private applicationUserService: ApplicationUserService
  ) {}

  open(raffle: IRaffle, proyect: IProyect): void {
    if (this.isOpen) {
      return;
    }
    this.isOpen = true;
    const modalRef: NgbModalRef = this.modalService.open(TicketComponent);
    modalRef.componentInstance.raffle = raffle;
    this.globalRaffle = raffle;
    modalRef.componentInstance.proyect = proyect;
    modalRef.result.finally(() => (this.isOpen = false));
  }

  createTicket(): void {
    const ticket = new Ticket();
    ticket.raffle = this.globalRaffle;

    this.accountService.identity().subscribe(account => {
      if (account) {
        this.account = account;

        this.applicationUserService
          .query({ 'internalUserId.equals': this.account.id })
          .subscribe((res: HttpResponse<IApplicationUser[]>) => {
            this.applicationUser = res.body || [];
            ticket.buyer = this.applicationUser[0];
            this.subscribeToSaveResponse(this.ticketService.create(ticket));
          });
      }
    });
  }
  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITicket>>): void {
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
