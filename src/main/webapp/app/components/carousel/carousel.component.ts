import { Component, Input, OnInit } from '@angular/core';
import { NgbCarouselConfig } from '@ng-bootstrap/ng-bootstrap';
import { IResource } from 'app/shared/model/resource.model';

@Component({
  selector: 'jhi-carousel',
  templateUrl: './carousel.component.html',
  styleUrls: ['./carousel.component.scss'],
})
export class CarouselComponent implements OnInit {
  @Input() items?: IResource[];
  @Input() interval = 0;
  @Input() keyboard?: boolean;
  @Input() width: any;
  @Input() height: any;
  firstTimeLoad = true;

  config: NgbCarouselConfig;

  constructor(config: NgbCarouselConfig) {
    this.config = config;
  }

  ngOnInit(): void {
    this.config.wrap = true;
    this.config.keyboard = this.keyboard ? this.keyboard : false;
    this.config.pauseOnHover = false;
  }

  GetVideoId(url: string | undefined): string {
    const urlParmas = new URL(url || '');
    return urlParmas.searchParams.get('v') || '';
  }
}
