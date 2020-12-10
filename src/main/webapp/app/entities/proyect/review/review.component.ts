import { HttpResponse } from '@angular/common/http';
import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { AccountService } from 'app/core/auth/account.service';
import { ApplicationUserService } from 'app/entities/application-user/application-user.service';
import { IApplicationUser } from 'app/shared/model/application-user.model';
import { IProyect } from 'app/shared/model/proyect.model';
import { IReview, Review } from 'app/shared/model/review.model';
import * as moment from 'moment';
import { ReviewService } from 'app/entities/review/review.service';
import { Observable } from 'rxjs';
import { ProyectService } from '../proyect.service';
@Component({
  selector: 'jhi-review-dialog',
  templateUrl: './review.component.html',
  styleUrls: ['../../../../content/scss/paper-dashboard.scss', 'review.component.scss'],
})
export class ReviewComponent implements OnInit {
  @Input() public proyect: IProyect | undefined;

  review = 0;
  authenticationError = false;
  isSaving = false;
  comment = '';
  touched = false;
  reviewForm = this.fb.group({
    id: [],
    review: [null, Validators.compose([Validators.required])],
    comment: [null, Validators.compose([Validators.required])],
  });
  account: any;
  applicationUser: IApplicationUser[] | undefined;

  constructor(
    public activeModal: NgbActiveModal,
    protected fb: FormBuilder,
    private accountService: AccountService,
    private applicationUserService: ApplicationUserService,
    protected reviewService: ReviewService,
    private proyectService: ProyectService
  ) {}

  ngOnInit(): void {
    this.accountService.identity().subscribe(account => {
      if (account) {
        this.account = account;

        this.applicationUserService
          .query({ 'internalUserId.equals': this.account.id })
          .subscribe((res: HttpResponse<IApplicationUser[]>) => {
            this.applicationUser = res.body || [];
          });
      }
    });
  }

  setReview(value: any): void {
    this.review = value;
  }

  saveReview(e: any): void {
    e.preventDefault();
    const review = this.createFromForm();
    this.reviewService.create(review).subscribe(() => {
      if (this.proyect) {
        this.reviewService.query({ 'proyectId.equals': this.proyect.id }).subscribe((res: HttpResponse<IReview[]>) => {
          this.calculateReviews(res.body!);
        });
      }
    });
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReview>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  onKeydownEvent(e: any): void {
    this.comment = e.currentTarget.value;
    this.touched = true;
  }

  calculateReviews(reviews: IReview[]): void {
    let sum = 0;

    for (let i = 0; i < reviews.length; i++) {
      sum += reviews[i].rating!;
    }

    const average = sum / reviews.length;

    if (isNaN(average)) {
      const review = this.createFromForm();
      this.proyect!.rating = review.rating;
    } else {
      this.proyect!.rating = Math.round(average);
    }

    this.subscribeToSaveResponse(this.proyectService.update(this.proyect!));

    this.activeModal.dismiss();

    setTimeout(() => {
      location.reload();
    }, 2000);
  }

  private createFromForm(): IReview {
    return {
      ...new Review(),
      id: undefined,
      timeStamp: moment(),
      message: this.comment,
      user: this.account.firstName + ' ' + this.account.lastName,
      rating: this.review,
      proyect: this.proyect,
    };
  }
}
