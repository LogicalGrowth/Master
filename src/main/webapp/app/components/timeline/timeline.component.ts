import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'jhi-timeline',
  templateUrl: './timeline.component.html',
  styleUrls: ['../../../content/scss/paper-dashboard.scss'],
})
export class TimelineComponent implements OnInit {
  @Input() title: any;
  @Input() timelineClass: any = '';
  @Input() cards: any;
  constructor(private router: Router) {}

  ngOnInit(): void {}

  editItem(url: any): void {
    this.router.navigate([url]);
  }
}
