import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProyect } from 'app/shared/model/proyect.model';
import { CheckpointService } from '../checkpoint/checkpoint.service';
import { PaymentService } from '../payment/payment.service';
import { ReviewService } from '../review/review.service';

@Component({
  selector: 'jhi-proyect-detail',
  templateUrl: './proyect-detail.component.html',
  styleUrls: ['../../../content/scss/paper-dashboard.scss', '../../../content/scss/paper-dashboard/rating/rating.scss'],
})
export class ProyectDetailComponent implements OnInit {
  proyect: IProyect | null = null;
  reviews: any;
  ratings = [1, 2, 3, 4, 5];
  position: any[] = [];
  hasMarker = false;
  percentile: any;
  rating: any;
  donors: any;
  checkpoints: any;
  cards: any[] = [];

  constructor(
    protected activatedRoute: ActivatedRoute,
    private reviewService: ReviewService,
    private paymentService: PaymentService,
    private checkPointService: CheckpointService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ proyect }) => {
      this.proyect = proyect;
      this.position.push(proyect.coordY);
      this.position.push(proyect.coordX);
      this.hasMarker = true;
      this.percentile = (100 * proyect.collected) / proyect.goalAmount;
      this.rating = (100 * proyect.rating) / 5;
      this.reviewService.findByProyect(proyect.id).subscribe(data => {
        this.reviews = data.body;
      });
      this.paymentService.findTopDonations(proyect.id).subscribe(data => {
        this.donors = data.body;
      });
      this.checkPointService.findByProyectId(proyect.id, this.percentile).subscribe(data => {
        if (data.body) {
          let i = 0;
          for (const checkpoint of data.body) {
            i++;
            const obj = {
              inverted: true,
              type: 'success',
              icon: 'nc-icon nc-sun-fog-29',
              subTitle: 'Checkpoint ' + i,
              body: checkpoint.message,
            };
            this.cards.push(obj);
          }
        }
      });
    });
  }
}
