import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'jhi-task-table',
  templateUrl: './task-table.component.html',
  styleUrls: ['../../../content/scss/paper-dashboard.scss']
})
export class TaskTableComponent implements OnInit {



  data:IRowData[] = [];


  @Input() tableTitle: any;
  @Input() tableDescription: any;
  @Input() tableClass: any;
  @Input() bottomDescription: any;
  @Input() items: any;
  @Input() firstIcon: any;
  @Input() secondIcon: any;
  @Input() bottomIcon: any;
  
  
  
  
  constructor() { }

  ngOnInit(): void {
    this.data = this.items;
  }

}

interface IRowData {
  img: string,
  textLeft: string
}