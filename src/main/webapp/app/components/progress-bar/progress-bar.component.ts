import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'jhi-progress-bar',
  templateUrl: './progress-bar.component.html',
  styleUrls: ['../../../content/scss/paper-dashboard.scss'],
})
export class ProgressBarComponent implements OnInit {
  @Input() progressBarClass: any;
  @Input() minValue: any;
  @Input() maxValue: any;
  @Input() currentValue: any;
  constructor() {}

  ngOnInit(): void {}
}
