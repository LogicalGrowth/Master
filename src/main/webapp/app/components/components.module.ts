import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SimpleButtonComponent } from './simple-button/simple-button.component';
import { IconButtonComponent } from './icon-button/icon-button.component';
import { SmallCardComponent } from './small-card/small-card.component';


@NgModule({
  declarations: [SimpleButtonComponent, IconButtonComponent, SmallCardComponent],
  imports: [
    CommonModule
  ],
  exports: [SimpleButtonComponent, IconButtonComponent, SmallCardComponent]  
})
export class ComponentsModule { }
