import { Component, OnInit } from '@angular/core';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/user/account.model';
import { RouteInfo, ROUTES } from './sidebar.constants';

/* eslint-disable @typescript-eslint/class-name-casing */
/* eslint-disable @typescript-eslint/camelcase */

const misc: any = {
  navbar_menu_visible: 0,
  active_collapse: true,
  disabled_collapse_init: 0,
};

@Component({
  selector: 'jhi-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['../../content/scss/paper-dashboard.scss'],
})
export class SidebarComponent implements OnInit {
  private userAccount?: Account;
  public menuItems?: RouteInfo[];
  isNavbarCollapsed = true;
  private toggleButton: any;
  private sidebarVisible?: boolean;
  public open = false;

  constructor(private accountService: AccountService) {
    this.sidebarVisible = false;
  }

  isNotMobileMenu(): boolean {
    if (window.outerWidth > 991) {
      return false;
    }
    return true;
  }

  ngOnInit(): void {
    this.menuItems = ROUTES.filter(menuItem => menuItem);

    this.accountService.identity().subscribe(account => {
      if (account) {
        this.userAccount = account;
      }
    });
  }

  isAuthenticated(): boolean {
    return this.accountService.isAuthenticated();
  }

  collapseNavbar(test: any): void {
    const selectedMenu = this.menuItems?.find(menu => menu.collapse === test);

    const index = this.menuItems!.findIndex(menu => menu.collapse === test);
    selectedMenu!.show = !selectedMenu!.show;

    this.menuItems![index] = selectedMenu!;
  }

  toggleNavbar(): void {
    this.isNavbarCollapsed = !this.isNavbarCollapsed;
  }

  getImageUrl(): string {
    return this.isAuthenticated() ? this.accountService.getImageUrl() : '';
  }

  getUserName(): string {
    return this.isAuthenticated() ? this.userAccount!.firstName : '';
  }

  minimizeSidebar(): void {
    const body = document.getElementsByTagName('body')[0];

    if (misc.sidebar_mini_active === true) {
      body.classList.remove('sidebar-mini');
      misc.sidebar_mini_active = false;
    } else {
      setTimeout(function (): void {
        body.classList.add('sidebar-mini');

        misc.sidebar_mini_active = true;
      }, 300);
    }

    // we simulate the window Resize so the charts will get updated in realtime.
    const simulateWindowResize = setInterval(function (): void {
      window.dispatchEvent(new Event('resize'));
    }, 180);

    // we stop the simulation of Window Resize after the animations are completed
    setTimeout(function (): void {
      clearInterval(simulateWindowResize);
    }, 1000);
  }

  sidebarOpen(): void {
    const toggleButton = this.toggleButton;
    const html = document.getElementsByTagName('html')[0];
    setTimeout(function (): void {
      toggleButton.classList.add('toggled');
    }, 500);
    const mainPanel = document.getElementsByClassName('main-panel')[0] as HTMLElement;
    if (window.innerWidth < 991) {
      mainPanel.style.position = 'fixed';
    }
    html.classList.add('nav-open');
    this.sidebarVisible = true;
  }

  sidebarClose(): void {
    const html = document.getElementsByTagName('html')[0];
    this.toggleButton.classList.remove('toggled');
    this.sidebarVisible = false;
    html.classList.remove('nav-open');
    const mainPanel = document.getElementsByClassName('main-panel')[0] as HTMLElement;

    if (window.innerWidth < 991) {
      setTimeout(function (): void {
        mainPanel.style.position = '';
      }, 500);
    }
  }

  sidebarToggle(): void {
    // var toggleButton = this.toggleButton;
    // var body = document.getElementsByTagName('body')[0];
    if (this.sidebarVisible === false) {
      this.sidebarOpen();
    } else {
      this.sidebarClose();
    }
  }
}
