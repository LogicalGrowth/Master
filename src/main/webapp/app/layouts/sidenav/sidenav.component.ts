import { MediaMatcher } from '@angular/cdk/layout';
import { ChangeDetectorRef, Component, OnDestroy, OnInit } from '@angular/core';
import { RouteInfo, ROUTES } from './sidebar.constants';
import { AccountService } from '../../core/auth/account.service';
import { Authority } from '../../shared/constants/authority.constants';
import { LoginModalService } from '../../core/login/login-modal.service';
import { LoginService } from '../../core/login/login.service';
import { Router } from '@angular/router';

@Component({
  selector: 'jhi-sidenav',
  templateUrl: './sidenav.component.html',
  styleUrls: ['./sidenav.component.scss'],
})
export class SidenavComponent implements OnInit, OnDestroy {
  shouldRun = true;
  fixedTopGap = '56';
  public menuItems?: RouteInfo[];

  mobileQuery: MediaQueryList;

  adminNav = [
    { name: 'Inicio', route: '/', icon: 'home' },
    { name: 'Usuarios', route: '/application-user', icon: 'perm_contact_calendar' },
    { name: 'Proyectos', route: '/proyect', icon: 'perm_contact_calendar' },
  ];

  userNav = [
    { name: 'Inicio', route: '/', icon: 'home' },
    { name: 'Recursos', route: '/resource', icon: 'perm_contact_calendar' },
  ];

  private _mobileQueryListener: () => void;

  constructor(
    changeDetectorRef: ChangeDetectorRef,
    media: MediaMatcher,
    private accountService: AccountService,
    private loginModalService: LoginModalService,
    private loginService: LoginService,
    private router: Router
  ) {
    this.mobileQuery = media.matchMedia('(max-width: 600px)');
    this._mobileQueryListener = () => changeDetectorRef.detectChanges();
    this.mobileQuery.addListener(this._mobileQueryListener);
  }

  ngOnInit(): void {
    this.menuItems = ROUTES.filter(menuItem => menuItem);
  }

  ngOnDestroy(): void {
    this.mobileQuery.removeListener(this._mobileQueryListener);
  }

  hideNav(): void {
    window.dispatchEvent(new Event('resize'));
  }

  collapseNavbar(test: any): void {
    const selectedMenu = this.menuItems?.find(menu => menu.collapse === test);

    const index = this.menuItems!.findIndex(menu => menu.collapse === test);
    selectedMenu!.show = !selectedMenu!.show;

    this.menuItems![index] = selectedMenu!;
  }

  isAdmin(): boolean {
    return this.accountService.hasAnyAuthority(Authority.ADMIN);
  }

  isAuthenticated(): boolean {
    return this.accountService.isAuthenticated();
  }

  login(): void {
    this.loginModalService.open();
  }

  logout(): void {
    this.loginService.logout();
    this.router.navigate(['']);
  }

  getImageUrl(): string {
    return this.isAuthenticated() ? this.accountService.getImageUrl() : '';
  }
}
