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
import { RegularCardAvatarComponent } from './regular-card-avatar/regular-card-avatar.component';
import { RegularCardImageComponent } from './regular-card-image/regular-card-image.component';

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
    RegularCardAvatarComponent,
    RegularCardImageComponent,
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
    RegularCardAvatarComponent,
    RegularCardImageComponent,
  ],
})
export class ComponentsModule {}
