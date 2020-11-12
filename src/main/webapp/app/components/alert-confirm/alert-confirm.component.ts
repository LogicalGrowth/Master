import { Component, Input, OnInit } from '@angular/core';
import Swal from 'sweetalert2';

@Component({
  selector: 'jhi-alert-confirm',
  templateUrl: './alert-confirm.component.html',
  styleUrls: ['./alert-confirm.component.scss'],
})
export class AlertConfirmComponent implements OnInit {
  @Input() title: any;
  @Input() text: any;
  @Input() confirmButtonText: any;
  @Input() buttonText: any;
  @Input() successTitle: any;
  @Input() successText: any;
  constructor() {}

  ngOnInit(): void {}

  showSwal(): void {
    Swal.fire({
      title: this.title,
      text: this.text,
      type: 'warning',
      showCancelButton: true,
      confirmButtonClass: 'btn btn-success',
      cancelButtonClass: 'btn btn-danger',
      confirmButtonText: this.confirmButtonText,
      buttonsStyling: false,
    });
  }

  showSuccess(): void {
    Swal.fire({
      title: this.successTitle,
      text: this.successText,
      type: 'success',
      confirmButtonClass: 'btn btn-success',
      buttonsStyling: false,
    });
  }
}
