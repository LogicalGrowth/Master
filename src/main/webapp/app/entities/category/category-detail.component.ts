import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICategory } from 'app/shared/model/category.model';

@Component({
  selector: 'jhi-category-detail',
  templateUrl: './category-detail.component.html',
  styleUrls: ['../../../content/scss/paper-dashboard.scss'],
})
export class CategoryDetailComponent implements OnInit {
  category: ICategory | null = null;
  imageSrc = '';
  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ category }) => (this.category = category));
    this.imageSrc = '';
  }

  previousState(): void {
    window.history.back();
  }

  onImageLoaded(data: any): void {
    this.imageSrc = data.secure_url;
  }
}
