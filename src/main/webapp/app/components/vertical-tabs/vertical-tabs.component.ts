import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'jhi-vertical-tabs',
  templateUrl: './vertical-tabs.component.html',
  styleUrls: ['../../../content/scss/paper-dashboard.scss'],
})
export class VerticalTabsComponent implements OnInit {
  @Input() titleTabs: any[] = [];
  @Input() paragraphs: any[] = [];

  constructor() {}

  ngOnInit(): void {}
}
