import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'jhi-icon-button',
  templateUrl: './icon-button.component.html',
  styleUrls: ['../../../content/scss/paper-dashboard.scss']
})
export class IconButtonComponent implements OnInit {

  @Input() label: any;
  @Input() type: any;
  @Input() icon: any;

  constructor() { }

  ngOnInit(): void {
  }

}
