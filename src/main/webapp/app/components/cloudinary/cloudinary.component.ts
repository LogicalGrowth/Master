import { Component, OnInit, Input, NgZone } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FileUploader, FileUploaderOptions } from 'ng2-file-upload';
import { Cloudinary } from '@cloudinary/angular-5.x';
import * as $ from 'jquery';

@Component({
  selector: 'jhi-cloudinary',
  templateUrl: './cloudinary.component.html',
  styleUrls: ['./cloudinary.component.scss'],
})
export class CloudinaryComponent implements OnInit {
  @Input() responses: Array<any>;
  @Input() okCallback: any;
  hasBaseDropZoneOver = false;
  uploader: any;
  title: string;
  progressBarContainer: any;
  progressBar: any;

  constructor(private cloudinary: Cloudinary, private zone: NgZone, private http: HttpClient) {
    this.responses = [];
    this.title = '';
  }
  ngOnInit(): void {
    // Create the file uploader, wire it to upload to your account
    const uploaderOptions: FileUploaderOptions = {
      url: `https://api.cloudinary.com/v1_1/${this.cloudinary.config().cloud_name}/upload`,
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
      this.showProgressBar();
      // Add Cloudinary's unsigned upload preset to the upload form
      form.append('upload_preset', this.cloudinary.config().upload_preset);
      // Add built-in and custom tags for displaying the uploaded photo in the list
      let tags = 'myphotoalbum';
      if (this.title) {
        form.append('context', `photo=${this.title}`);
        tags = `myphotoalbum,${this.title}`;
      }
      // Upload to a custom folder
      // Note that by default, when uploading via the API, folders are not automatically created in your Media Library.
      // In order to automatically create the folders based on the API requests,
      // please go to your account upload settings and set the 'Auto-create folders' option to enabled.
      form.append('folder', 'fun4found');
      // Add custom tags
      form.append('tags', tags);
      // Add file to upload
      form.append('file', fileItem);

      // Use default "withCredentials" value for CORS requests
      fileItem.withCredentials = false;
      return { fileItem, form };
    };

    // Update model on completion of uploading a file
    this.uploader.onCompleteItem = (item: any, response: string) => {
      this.progressBar.set(96, 'Verificando imágen.', false);
      this.progressBar.set(100, 'Imágen guardada.', false);
      setTimeout(() => {
        this.okCallback(JSON.parse(response));
      }, 1000);
    };
    this.initProgressBar();
  }

  initProgressBar(): void {
    this.progressBarContainer = $('#progress-bar');
    this.progressBar = {
      chain: [] as any[],
      progress: this.progressBarContainer.children('.progress'),
      progressBar: this.progressBarContainer.find('.progress-bar'),
      progressInfo: this.progressBarContainer.children('.progress-info'),
      set(value: number, info: string, noPush: any): void {
        if (!noPush) {
          this.chain.push(value);
        }
        if (this.chain[0] === value) {
          this.go(value, info);
        } else {
          // eslint-disable-next-line @typescript-eslint/no-this-alias
          const self = this;
          setTimeout(() => {
            self.set(value, info, true);
          }, 200);
        }
      },
      go(value: any, info: any): void {
        this.progressInfo.text(info);
        // eslint-disable-next-line @typescript-eslint/no-this-alias
        const self = this;
        const interval = setInterval(() => {
          let curr: any = self.progress.attr('value');
          if (curr >= value) {
            clearInterval(interval);
            self.progress.attr('value', value);
            self.progressBar.css('width', value + '%');
            self.progressBar.html(value + '%');
            self.chain.shift();
          } else {
            self.progress.attr('value', ++curr);
            self.progressBar.css('width', curr + '%');
          }
        }, 10);
      },
    };
  }

  showProgressBar(): void {
    this.progressBar.set(5, 'Preparando la imágen', false);
    this.progressBar.set(22, 'Conectando con el servidor', false);
    this.progressBar.set(52, 'Instalando las dependencias', false);
    this.progressBar.set(82, 'Subiendo imágen', false);
  }

  updateTitle(value: string): void {
    this.title = value;
  }

  fileOverBase(e: any): void {
    this.hasBaseDropZoneOver = e;
  }

  getFileProperties(fileProperties: any): any {
    // Transforms Javascript Object to an iterable to be used by *ngFor
    if (!fileProperties) {
      return null;
    }
    return Object.keys(fileProperties).map(key => ({ key, value: fileProperties[key] }));
  }
}
