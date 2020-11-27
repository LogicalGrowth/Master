import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'jhi-small-card',
  templateUrl: './small-card.component.html',
  styleUrls: ['../../../content/scss/paper-dashboard.scss'],
})
export class SmallCardComponent implements OnInit {
  @Input() title: any;
  @Input() description: any;
  @Input() icon: any;
  constructor() {}

  ngOnInit(): void {}
}
