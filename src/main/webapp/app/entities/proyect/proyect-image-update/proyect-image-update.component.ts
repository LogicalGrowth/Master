import { Component, OnInit } from '@angular/core';
import * as $ from 'jquery';
import * as boostrap from 'bootstrap';
import { FormBuilder, Validators } from '@angular/forms';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IProyect } from 'app/shared/model/proyect.model';
import { ResourceService } from 'app/entities/resource/resource.service';
import { IResource, Resource } from 'app/shared/model/resource.model';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'jhi-proyect-image-update',
  templateUrl: './proyect-image-update.component.html',
  styleUrls: ['./proyect-image-update.component.scss', '../../../../content/scss/paper-dashboard.scss'],
})
export class ProyectImageUpdateComponent implements OnInit {
  isSaving = false;
  proyect: IProyect | undefined;
  images: IResource[] = [];
  regexYoutube = /^(https?:\/\/)?((www\.)?youtube\.com|youtu\.?be)\/.+$/;

  editForm = this.fb.group({
    url: [null, Validators.compose([Validators.required, Validators.maxLength(255), Validators.pattern(this.regexYoutube)])],
  });

  constructor(
    protected activatedRoute: ActivatedRoute,
    private resourceService: ResourceService,
    private fb: FormBuilder,
    private router: Router
  ) {}

  ngOnInit(): void {
    boostrap.Alert;
    this.activatedRoute.data.subscribe(({ proyect }) => {
      this.proyect = proyect;
      this.resourceService
        .query({ 'proyectId.equals': proyect.id })
        .subscribe((res: HttpResponse<IResource[]>) => (this.images = res.body || []));
    });
  }

  saveImage(data: any): void {
    const image = this.createFromForm(data.secure_url, 'Image');
    this.subscribeToSaveResponse(this.resourceService.create(image));
  }

  save(): void {
    this.isSaving = true;
    const image = this.createFromForm('', 'Youtube');
    this.subscribeToSaveResponse(this.resourceService.create(image));
  }

  goToProyect(): void {
    this.router.navigate(['/proyect/' + this.proyect?.id + '/view']);
  }

  private createFromForm(url = '', type = ''): IResource {
    return {
      ...new Resource(),
      url: url ? url : this.editForm.get(['url'])!.value,
      type: type ? type : this.editForm.get(['type'])!.value,
      proyect: this.proyect,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IResource>>): void {
    result.subscribe(
      e => this.onSaveSuccess(e),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(e: HttpResponse<IResource>): void {
    this.images.push(e.body!);
    $('#field_url').val('');
    $('#type').val('');
    this.isSaving = false;
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
