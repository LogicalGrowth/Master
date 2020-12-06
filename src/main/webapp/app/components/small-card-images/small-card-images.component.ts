import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'jhi-small-card-images',
  templateUrl: './small-card-images.component.html',
  styleUrls: ['../../../content/scss/paper-dashboard.scss'],
})
export class SmallCardImagesComponent implements OnInit {
  @Input() title: any;
  @Input() description: any;
  @Input() url: any;
  constructor() {}

  ngOnInit(): void {}
}
