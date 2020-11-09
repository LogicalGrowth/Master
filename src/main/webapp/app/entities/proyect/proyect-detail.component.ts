import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProyect } from 'app/shared/model/proyect.model';
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
  cards: any[] = [
    {
      inverted: true,
      type: 'danger',
      icon: 'nc-icon nc-single-copy-04',
      subTitle: 'sub',
      body: 'body',
    },
    {
      inverted: true,
      type: 'success',
      icon: 'nc-icon nc-sun-fog-29',
      subTitle: 'sub',
      body: 'body',
    },
  ];

  constructor(protected activatedRoute: ActivatedRoute, private reviewService: ReviewService) {}

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
    });
  }
}
