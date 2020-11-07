import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SmallCardComponent } from './small-card/small-card.component';
import { NotificationComponent } from './notification/notification.component';
import { SimpleButtonComponent } from './simple-button/simple-button.component';
import { IconButtonComponent } from './icon-button/icon-button.component';

@NgModule({
  declarations: [SmallCardComponent, NotificationComponent, SimpleButtonComponent, IconButtonComponent],
  imports: [CommonModule],
  exports: [SmallCardComponent, NotificationComponent, SimpleButtonComponent, IconButtonComponent],
})
export class ComponentsModule {}
