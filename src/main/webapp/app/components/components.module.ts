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
  imports: [CommonModule],
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
