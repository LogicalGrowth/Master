import { Component, Input, OnInit } from '@angular/core';
import * as $ from 'jquery';
import 'bootstrap-notify';

@Component({
  selector: 'jhi-notification-state',
  templateUrl: './notification.component.html',
  styleUrls: ['../../../content/scss/paper-dashboard.scss'],
})
export class NotificationComponent implements OnInit {
  @Input() icon: any;
  @Input() message: any;
  @Input() type: any;
  constructor() {}

  ngOnInit(): void {
    this.showNotification();
  }

  showNotification(): void {
    const messageType =
      this.type === 'warning' ? 'Alerta: ' : this.type === 'danger' ? 'Error: ' : this.type === 'success' ? 'Éxito: ' : 'Información: ';
    $.notify(
      {
        icon: this.icon,
        title: messageType,
        message: this.message,
      },
      {
        type: this.type,
        timer: 1500,
        placement: {
          from: 'top',
          align: 'right',
        },
        template:
          '<div data-notify="container" class="col-11 col-md-4 alert alert-{0} alert-with-icon" role="alert">' +
          '<button type="button" aria-hidden="true" class="close" data-notify="dismiss">' +
          '<i class="fa fa-times"></i>' +
          '</button>' +
          '<strong><span data-notify="title">{1}</span></strong>' +
          '<span data-notify="message">{2}</span></div>',
      }
    );
  }
}
