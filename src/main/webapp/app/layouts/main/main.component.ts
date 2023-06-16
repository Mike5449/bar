import { Component, OnInit, RendererFactory2, Renderer2 } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Router, ActivatedRouteSnapshot, NavigationEnd } from '@angular/router';
import { TranslateService, LangChangeEvent } from '@ngx-translate/core';
import dayjs from 'dayjs/esm';

import { AccountService } from 'app/core/auth/account.service';
import { LoginService } from 'app/login/login.service';
import { TokenDecodeService } from 'app/shared/token-decode.service';
import { SharedService } from 'app/shared/shared.service';

@Component({
  selector: 'jhi-main',
  templateUrl: './main.component.html',
  styleUrls:['./main.component.scss']
})
export class MainComponent implements OnInit {
  private renderer: Renderer2;
  closeSideBar?:boolean;

  infoToken:any;
  constructor(
    private accountService: AccountService,
    private titleService: Title,
    private router: Router,
    private translateService: TranslateService,
    rootRenderer: RendererFactory2,
    private loginService: LoginService,
    private tokenDecodeService:TokenDecodeService
  ) {
    this.renderer = rootRenderer.createRenderer(document.querySelector('html'), null);
  }

  ngOnInit(): void {
    this.infoToken=this.tokenDecodeService.getTokenInformation();
    console.log(this.infoToken);

    // try to log in automatically
    this.accountService.identity().subscribe();

    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.updateTitle();
      }
    });

    this.translateService.onLangChange.subscribe((langChangeEvent: LangChangeEvent) => {
      this.updateTitle();
      dayjs.locale(langChangeEvent.lang);
      this.renderer.setAttribute(document.querySelector('html'), 'lang', langChangeEvent.lang);
    });

    SharedService.closeSideBar.subscribe(data=>{
      this.closeSideBar=data;
    })
  }

  private getPageTitle(routeSnapshot: ActivatedRouteSnapshot): string {
    const title: string = routeSnapshot.data['pageTitle'] ?? '';
    if (routeSnapshot.firstChild) {
      return this.getPageTitle(routeSnapshot.firstChild) || title;
    }
    return title;
  }

  private updateTitle(): void {
    let pageTitle = this.getPageTitle(this.router.routerState.snapshot.root);
    if (!pageTitle) {
      pageTitle = 'global.title';
    }
    this.translateService.get(pageTitle).subscribe(title => this.titleService.setTitle(title));
  }

  logout(): void {
   
    this.loginService.logout();
    this.router.navigate(['']);
    console.log(this.infoToken)
  }

  firstChange() {
    const filterMenu = document.querySelector<HTMLElement>(".filter-menu");
    if (filterMenu) {
      filterMenu.classList.toggle("active");
    }
  }

  secondChange() {
    const listElement = document.querySelector<HTMLElement>(".list");
    const gridElement = document.querySelector<HTMLElement>(".grid");
    const productsAreaWrapperElement = document.querySelector<HTMLElement>(
      ".products-area-wrapper"
    );

    if (listElement && gridElement && productsAreaWrapperElement) {
      listElement.classList.remove("active");
      gridElement.classList.add("active");
      productsAreaWrapperElement.classList.add("gridView");
      productsAreaWrapperElement.classList.remove("tableView");
    }
  }

  thirdChange() {
    const listElement = document.querySelector<HTMLElement>(".list");
const gridElement = document.querySelector<HTMLElement>(".grid");
const productsAreaWrapperElement = document.querySelector<HTMLElement>(
  ".products-area-wrapper"
);

if (listElement && gridElement && productsAreaWrapperElement) {
  listElement.addEventListener("click", () => {
    listElement.classList.add("active");
    gridElement.classList.remove("active");
    productsAreaWrapperElement.classList.remove("gridView");
    productsAreaWrapperElement.classList.add("tableView");
  });
}

  }

  changeTheme() {
    const modeSwitch = document.querySelector<HTMLElement>(".mode-switch");
    if (modeSwitch) {
      modeSwitch.classList.toggle("active");
    }
  }
}
