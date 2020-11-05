import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'jhi-simple-button',
  templateUrl: './simple-button.component.html',
  styleUrls: ['../../../content/scss/paper-dashboard.scss']
})
export class SimpleButtonComponent implements OnInit {

  @Input() label: any;
  @Input() type: any;
  
  constructor() { }

  ngOnInit(): void {
  }

}
