import { Component, Input, NgZone, OnInit } from '@angular/core';
import { FileUploader, FileUploaderOptions } from 'ng2-file-upload';
import { HttpClient } from '@angular/common/http';
import { Cloudinary } from '@cloudinary/angular-5.x';

@Component({
  selector: 'jhi-cloudinary',
  templateUrl: './cloudinary.component.html',
  styleUrls: ['./cloudinary.component.scss'],
})
export class CloudinaryComponent implements OnInit {
  @Input() responses: Array<any>;

  hasBaseDropZoneOver = false;
  @Input() uploader: FileUploader | any;
  title: string;

  constructor(private cloudinary: Cloudinary, private zone: NgZone, private http: HttpClient) {
    this.responses = [];
    this.title = '';
  }

  ngOnInit(): void {
    // Create the file uploader, wire it to upload to your account
    const uploaderOptions: FileUploaderOptions = {
      url: `https://api.cloudinary.com/v1_1/barnesnoble/upload`,
      // Upload files automatically upon addition to upload queue
      autoUpload: true,
      // Use xhrTransport in favor of iframeTransport
      isHTML5: true,
      // Calculate progress independently for each uploaded file
      removeAfterUpload: true,
      // XHR request headers
      headers: [
        {
          name: 'X-Requested-With',
          value: 'XMLHttpRequest',
        },
      ],
    };

    this.uploader = new FileUploader(uploaderOptions);

    this.uploader.onBuildItemForm = (fileItem: any, form: FormData): any => {
      // Add Cloudinary unsigned upload preset to the upload form
      form.append('upload_preset', this.cloudinary.config().upload_preset);

      // Add file to upload
      form.append('file', fileItem);

      // Use default "withCredentials" value for CORS requests
      fileItem.withCredentials = false;
      return { fileItem, form };
    };
  }

  fileOverBase(e: any): void {
    this.hasBaseDropZoneOver = e;
  }
}
