import { Component, OnInit } from '@angular/core';
import { NgxSpinnerService } from 'ngx-spinner';
import { ProyectService } from '../proyect.service';
import * as $ from 'jquery';
import * as boostrap from 'bootstrap';
import { FormBuilder, Validators } from '@angular/forms';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IProyect } from 'app/shared/model/proyect.model';
import { ResourceService } from 'app/entities/resource/resource.service';
import { IResource, Resource } from 'app/shared/model/resource.model';

@Component({
  selector: 'jhi-proyect-image-update',
  templateUrl: './proyect-image-update.component.html',
  styleUrls: ['./proyect-image-update.component.scss', '../../../../content/scss/paper-dashboard.scss'],
})
export class ProyectImageUpdateComponent implements OnInit {
  imagen: any;
  imagenMin: any;
  isSaving = false;
  proyect: IProyect | undefined;

  editForm = this.fb.group({
    url: [null, [Validators.required, Validators.maxLength(255)]],
    type: [null, [Validators.required, Validators.maxLength(255)]],
  });

  constructor(
    private proyectService: ProyectService,
    private spinner: NgxSpinnerService,
    private resourceService: ResourceService,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    boostrap.Button;
    this.proyectService.find(window.sessionStorage.proyect).subscribe(
      data => {
        this.proyect = data.body || undefined;
      },
      error => {
        alert(error);
      }
    );
  }

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
        this.imagenMin = '';
      },
      (err: any) => {
        alert(err.error.mensaje);
        this.spinner.hide();
      }
    );
  }

  save(): void {
    this.isSaving = true;
    const image = this.createFromForm();
    this.subscribeToSaveResponse(this.resourceService.create(image));
  }

  private createFromForm(): IResource {
    return {
      ...new Resource(),
      url: this.editForm.get(['url'])!.value,
      type: this.editForm.get(['type'])!.value,
      proyect: this.proyect,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IResource>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    $('#field_url').val('');
    $('#type').val('');
    this.isSaving = false;
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
