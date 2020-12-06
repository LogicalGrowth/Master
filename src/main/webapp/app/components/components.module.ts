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
import { CarouselComponent } from './carousel/carousel.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { StatePipe } from './state.pipe';
import { LastUpdatePipe } from './lastUpdate.pipe';
import { PaypalButtonComponent } from './paypal-button/paypal-button.component';
import { YouTubePlayerModule } from '@angular/youtube-player';
import { ChartLineComponent } from './chart-line/chart-line.component';

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
    CarouselComponent,
    StatePipe,
    LastUpdatePipe,
    PaypalButtonComponent,
    ChartLineComponent,
  ],
  imports: [
    CommonModule,
    // eslint-disable-next-line @typescript-eslint/camelcase
    CloudinaryModule.forRoot(cloudinary, { cloud_name: 'dbk8m5dcv', upload_preset: 'cq8ymdc5' }),
    FileUploadModule,
    YouTubePlayerModule,
    NgbModule,
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
    CarouselComponent,
    StatePipe,
    LastUpdatePipe,
    PaypalButtonComponent,
    ChartLineComponent,
  ],
})
export class ComponentsModule {}
