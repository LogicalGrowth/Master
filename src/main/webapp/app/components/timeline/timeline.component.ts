import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'jhi-timeline',
  templateUrl: './timeline.component.html',
  styleUrls: ['../../../content/scss/paper-dashboard.scss'],
})
export class TimelineComponent implements OnInit {
  @Input() title: any;
  @Input() timelineClass: any = '';
  @Input() cards: any;
  constructor() {}

  ngOnInit(): void {}
}
