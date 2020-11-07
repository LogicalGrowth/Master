import { Component, OnInit } from '@angular/core';
import * as noUiSlider from 'nouislider';

@Component({
  selector: 'jhi-slider-bar',
  templateUrl: './slider-bar.component.html',
  styleUrls: ['../../../content/scss/paper-dashboard.scss'],
})
export class SliderBarComponent implements OnInit {
  constructor() {}

  ngOnInit(): void {
    const sliderRegular = document.getElementById('sliderRegular') as HTMLElement;
    noUiSlider.create(sliderRegular, {
      start: 40,
      connect: [true, false],
      range: {
        min: 0,
        max: 100,
      },
    });
  }
}
