import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { first, take } from 'rxjs/operators';

import { LoginModalService } from 'app/core/login/login-modal.service';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/user/account.model';
import { ProyectService } from 'app/entities/proyect/proyect.service';
import { IProyect } from 'app/shared/model/proyect.model';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['home.scss', '../../content/scss/paper-dashboard.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {
  account: Account | null = null;
  authSubscription?: Subscription;
  proyects?: IProyect[];

  constructor(
    private accountService: AccountService,
    private loginModalService: LoginModalService,
    protected proyectService: ProyectService
  ) {}

  ngOnInit(): void {
    this.authSubscription = this.accountService.getAuthenticationState().subscribe(account => (this.account = account));
    this.proyectService.query({}).subscribe((res: HttpResponse<IProyect[]>) => {
      if (res.body) {
        this.proyects = this.shuffleArray(res.body).slice(0, 3) || [];
      }
    });
  }

  isAuthenticated(): boolean {
    return this.accountService.isAuthenticated();
  }

  login(): void {
    this.loginModalService.open();
  }

  ngOnDestroy(): void {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }

  shuffleArray(array: IProyect[]): IProyect[] {
    let m = array.length,
      t,
      i;

    // While there remain elements to shuffle
    while (m) {
      // Pick a remaining element…
      i = Math.floor(Math.random() * m--);

      // And swap it with the current element.
      t = array[m];
      array[m] = array[i];
      array[i] = t;
    }

    return array;
  }
}
