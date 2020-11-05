import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'jhi-task-table',
  templateUrl: './task-table.component.html',
  styleUrls: ['../../../content/scss/paper-dashboard.scss'],
})
export class TaskTableComponent implements OnInit {
  data: IRowData[] = [];
  dataButton: IRowButtons[] = [];

  @Input() tableTitle: any;
  @Input() tableDescription: any;
  @Input() tableClass: any;
  @Input() bottomDescription: any;
  @Input() items: any;
  @Input() firstIcon: any;
  @Input() secondIcon: any;
  @Input() bottomIcon: any;
  @Input() buttons: any;

  constructor() {}

  ngOnInit(): void {
    this.data = this.items;
    this.dataButton = this.buttons;
  }
}

interface IRowData {
  img: string;
  textLeft: string;
}

interface IRowButtons {
  name: string;
  class: string;
  icon: string;
  onClick: string;
}
