import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ProyectService } from '../proyect.service';

@Component({
  selector: 'jhi-proyect-image-update',
  templateUrl: './proyect-image-update.component.html',
  styleUrls: ['./proyect-image-update.component.scss'],
})
export class ProyectImageUpdateComponent implements OnInit {
  imagen: any;
  imagenMin: any;

  constructor(private proyectService: ProyectService, private router: Router, private spinner: NgxSpinnerService) {}

  ngOnInit(): void {}

  onFileChange(event: any): void {
    this.imagen = event.target.files[0];
    const fr = new FileReader();
    fr.onload = (evento: any) => {
      this.imagenMin = evento.target.result;
    };
    fr.readAsDataURL(this.imagen);
  }

  onUpload(): void {
    this.spinner.show();
    this.proyectService.uploadImage(this.imagen, window.sessionStorage.proyect).subscribe(
      () => {
        this.spinner.hide();
        this.imagen = null;
      },
      (err: any) => {
        alert(err.error.mensaje);
        this.spinner.hide();
      }
    );
  }
}
