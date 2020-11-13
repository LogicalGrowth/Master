import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SmallCardComponent } from './small-card/small-card.component';
import { NotificationComponent } from './notification/notification.component';
import { SimpleButtonComponent } from './simple-button/simple-button.component';
import { IconButtonComponent } from './icon-button/icon-button.component';
import { IconComponent } from './icon/icon.component';
import { ProgressBarComponent } from './progress-bar/progress-bar.component';
import { SliderBarComponent } from './slider-bar/slider-bar.component';
import { TimelineComponent } from './timeline/timeline.component';
import { VerticalTabsComponent } from './vertical-tabs/vertical-tabs.component';
import { CloudinaryComponent } from './cloudinary/cloudinary.component';
import { CloudinaryModule } from '@cloudinary/angular-5.x';
import * as cloudinary from 'cloudinary-core';
import { FileUploadModule } from 'ng2-file-upload';

@NgModule({
  declarations: [
    SmallCardComponent,
    NotificationComponent,
    SimpleButtonComponent,
    IconButtonComponent,
    IconComponent,
    ProgressBarComponent,
    SliderBarComponent,
    TimelineComponent,
    VerticalTabsComponent,
    CloudinaryComponent,
  ],
  imports: [
    CommonModule,
    // eslint-disable-next-line @typescript-eslint/camelcase
    CloudinaryModule.forRoot(cloudinary, { cloud_name: 'barnesnoble', upload_preset: 'cq8ymdc5' }),
    FileUploadModule,
  ],
  exports: [
    SmallCardComponent,
    NotificationComponent,
    SimpleButtonComponent,
    IconButtonComponent,
    IconComponent,
    ProgressBarComponent,
    SliderBarComponent,
    TimelineComponent,
    VerticalTabsComponent,
    CloudinaryComponent,
  ],
})
export class ComponentsModule {}
