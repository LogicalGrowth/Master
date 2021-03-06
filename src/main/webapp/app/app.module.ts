import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { Fun4FundSharedModule } from 'app/shared/shared.module';
import { Fun4FundCoreModule } from 'app/core/core.module';
import { Fun4FundAppRoutingModule } from './app-routing.module';
import { Fun4FundHomeModule } from './home/home.module';
import { Fun4FundEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ActiveMenuDirective } from './layouts/navbar/active-menu.directive';
import { ErrorComponent } from './layouts/error/error.component';
import { SidenavComponent } from './layouts/sidenav/sidenav.component';
import { DemoMaterialModule } from './layouts/sidenav/material-module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatNativeDateModule } from '@angular/material/core';
@NgModule({
  imports: [
    BrowserModule,
    Fun4FundSharedModule,
    Fun4FundCoreModule,
    Fun4FundHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    Fun4FundEntityModule,
    Fun4FundAppRoutingModule,
    DemoMaterialModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    MatNativeDateModule,
  ],
  declarations: [
    MainComponent,
    NavbarComponent,
    ErrorComponent,
    PageRibbonComponent,
    ActiveMenuDirective,
    FooterComponent,
    SidenavComponent,
  ],
  bootstrap: [MainComponent, SidenavComponent],
})
export class Fun4FundAppModule {}
