import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICategory } from 'app/shared/model/category.model';
import { CategoryService } from './category.service';
import { CategoryDeleteDialogComponent } from './category-delete-dialog.component';
import { AccountService } from 'app/core/auth/account.service';
import { ApplicationUserService } from '../application-user/application-user.service';
import { User } from '../../core/user/user.model';

@Component({
  selector: 'jhi-category',
  templateUrl: './category.component.html',
  styleUrls: ['../../../content/scss/paper-dashboard.scss'],
})
export class CategoryComponent implements OnInit, OnDestroy {
  categories?: ICategory[];
  eventSubscriber?: Subscription;
  isProjectOwner!: Boolean;
  account!: User;

  constructor(
    protected categoryService: CategoryService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    private accountService: AccountService,
    private applicationUserService: ApplicationUserService
  ) {}

  loadAll(): void {
    this.categoryService.query().subscribe((res: HttpResponse<ICategory[]>) => (this.categories = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInCategories();

    this.accountService.identity().subscribe(account => {
      if (account) {
        this.account = account;
        this.applicationUserService.find(this.account.id).subscribe(applicationUser => {
          this.isProjectOwner = applicationUser.body?.admin ? true : false;
        });
      }
    });
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ICategory): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInCategories(): void {
    this.eventSubscriber = this.eventManager.subscribe('categoryListModification', () => this.loadAll());
  }

  delete(category: ICategory): void {
    const modalRef = this.modalService.open(CategoryDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.category = category;
  }
}
