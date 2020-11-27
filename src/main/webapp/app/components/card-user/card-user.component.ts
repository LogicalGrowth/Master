import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'jhi-card-user',
  templateUrl: './card-user.component.html',
  styleUrls: ['../../../content/scss/paper-dashboard.scss'],
})
export class CardUserComponent implements OnInit {
  @Input() img: any;
  @Input() name: any;
  @Input() item1: any;
  @Input() item2: any;
  @Input() item3: any;
  @Input() description1: any;
  @Input() description2: any;
  @Input() description3: any;
  constructor() {}

  ngOnInit(): void {}
}
