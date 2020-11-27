import { DatePipe } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { NgModule } from '@angular/core';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { SessionStorageService, LocalStorageService } from 'ngx-webstorage';
import { JhiLanguageService, JhiDataUtils, JhiDateUtils, JhiEventManager, JhiAlertService, JhiParseLinks } from 'ng-jhipster';

import { MockLanguageService } from './helpers/mock-language.service';
import { MockActivatedRoute, MockRouter } from './helpers/mock-route.service';
import { MockActiveModal } from './helpers/mock-active-modal.service';
import { MockAlertService } from './helpers/mock-alert.service';
import { MockEventManager } from './helpers/mock-event-manager.service';

@NgModule({
  providers: [
    DatePipe,
    JhiDataUtils,
    JhiDateUtils,
    JhiParseLinks,
    {
      provide: JhiLanguageService,
      useClass: MockLanguageService,
    },
    {
      provide: JhiEventManager,
      useClass: MockEventManager,
    },
    {
      provide: NgbActiveModal,
      useClass: MockActiveModal,
    },
    {
      provide: ActivatedRoute,
      useValue: new MockActivatedRoute({ id: 123 }),
    },
    {
      provide: Router,
      useClass: MockRouter,
    },
    {
      provide: JhiAlertService,
      useClass: MockAlertService,
    },
    {
      provide: NgbModal,
      useValue: null,
    },
    {
      provide: SessionStorageService,
      useValue: null,
    },
    {
      provide: LocalStorageService,
      useValue: null,
    },
  ],
  imports: [HttpClientTestingModule],
})
export class Fun4FundTestModule {}
