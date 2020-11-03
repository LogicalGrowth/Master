import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPartnerRequest } from 'app/shared/model/partner-request.model';

@Component({
  selector: 'jhi-partner-request-detail',
  templateUrl: './partner-request-detail.component.html',
})
export class PartnerRequestDetailComponent implements OnInit {
  partnerRequest: IPartnerRequest | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ partnerRequest }) => (this.partnerRequest = partnerRequest));
  }

  previousState(): void {
    window.history.back();
  }
}
