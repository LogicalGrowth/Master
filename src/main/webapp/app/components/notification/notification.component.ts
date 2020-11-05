import { Component, Input, OnInit } from '@angular/core';
declare var $: any;

@Component({
  selector: 'jhi-notification-state',
  templateUrl: './notification.component.html',
  styleUrls: ['./notification.component.scss'],
})
export class NotificationComponent implements OnInit {
  @Input() icon: any;
  @Input() message: any;
  @Input() title: any;
  @Input() from: any;
  @Input() align: any;
  @Input() type: any;
  constructor() {}

  ngOnInit(): void {
    this.showNotification();
  }

  showNotification(): void {
    $.notify(
      {
        icon: this.icon,
        title: this.title,
        message: this.message,
      },
      {
        type: this.type,
        timer: 2000,
        placement: {
          from: this.from,
          align: this.align,
        },
        template:
          '<div data-notify="container" class="col-11 col-md-4 alert alert-{0} alert-with-icon" role="alert">}' +
          '<button type="button" aria-hidden="true" class="close" data-notify="dismiss">' +
          '<i class="nc-icon nc-simple-remove"></i>' +
          '</button>' +
          '<span data-notify="icon" class="nc-icon nc-bell-55"></span> ' +
          '<span data-notify="title">{1}</span> ' +
          '<span data-notify="message">{2}</span>',
      }
    );
  }
}
