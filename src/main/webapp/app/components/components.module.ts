import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SmallCardComponent } from './small-card/small-card.component';
import { NotificationComponent } from './notification/notification.component';

@NgModule({
  declarations: [SmallCardComponent, NotificationComponent],
  imports: [CommonModule],
  exports: [SmallCardComponent, NotificationComponent],
})
export class ComponentsModule {}
