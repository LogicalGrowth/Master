import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'jhi-icon',
  templateUrl: './icon.component.html',
  styleUrls: ['../../../content/scss/paper-dashboard.scss'],
})
export class IconComponent implements OnInit {
  @Input() buttonCssClass: any;
  @Input() iconCssClass: any;
  @Input() functionClick: any;

  constructor() {}

  ngOnInit(): void {}
}
