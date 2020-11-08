import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SmallCardComponent } from './small-card/small-card.component';
import { NotificationComponent } from './notification/notification.component';
import { SimpleButtonComponent } from './simple-button/simple-button.component';
import { IconButtonComponent } from './icon-button/icon-button.component';
import { TaskTableComponent } from './task-table/task-table.component';

@NgModule({
  declarations: [SmallCardComponent, NotificationComponent, SimpleButtonComponent, IconButtonComponent, TaskTableComponent],
  imports: [CommonModule],
  exports: [SmallCardComponent, NotificationComponent, SimpleButtonComponent, IconButtonComponent, TaskTableComponent],
})
export class ComponentsModule {}
