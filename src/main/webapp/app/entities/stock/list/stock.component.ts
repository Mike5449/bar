import { Component, OnInit } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { combineLatest, filter, Observable, switchMap, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IStock } from '../stock.model';

import { ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { ASC, DESC, SORT, ITEM_DELETED_EVENT, DEFAULT_SORT_DATA } from 'app/config/navigation.constants';
import { EntityArrayResponseType, StockService } from '../service/stock.service';
import { StockDeleteDialogComponent } from '../delete/stock-delete-dialog.component';

@Component({
  selector: 'jhi-stock',
  templateUrl: './stock.component.html',
  styleUrls:['./stock.component.scss']
})
export class StockComponent implements OnInit {
  stocks?: IStock[];
  isLoading = false;

  predicate = 'id';
  ascending = true;

  itemsPerPage = ITEMS_PER_PAGE;
  totalItems = 0;
  page = 1;

  constructor(
    protected stockService: StockService,
    protected activatedRoute: ActivatedRoute,
    public router: Router,
    protected modalService: NgbModal
  ) {}

  trackId = (_index: number, item: IStock): number => this.stockService.getStockIdentifier(item);

  ngOnInit(): void {
    this.load();
  }

  delete(stock: IStock): void {
    const modalRef = this.modalService.open(StockDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.stock = stock;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        switchMap(() => this.loadFromBackendWithRouteInformations())
      )
      .subscribe({
        next: (res: EntityArrayResponseType) => {
          this.onResponseSuccess(res);
        },
      });
  }

  load(): void {
    this.loadFromBackendWithRouteInformations().subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onResponseSuccess(res);
      },
    });
  }

  navigateToWithComponentValues(): void {
    this.handleNavigation(this.page, this.predicate, this.ascending);
  }

  navigateToPage(page = this.page): void {
    this.handleNavigation(page, this.predicate, this.ascending);
  }

  getImageDataUrl(img:any,contentType:any){
    return `data:${contentType};base64,${img}`

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
    const modeSwitch = document.querySelector<HTMLElement>('.mode-switch');

    if (modeSwitch) {
      modeSwitch.addEventListener('click', () => {
        document.documentElement.classList.toggle('light');
        modeSwitch.classList.toggle('active');
      });
    }
  }

  protected loadFromBackendWithRouteInformations(): Observable<EntityArrayResponseType> {
    return combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data]).pipe(
      tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
      switchMap(() => this.queryBackend(this.page, this.predicate, this.ascending))
    );
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    const page = params.get(PAGE_HEADER);
    this.page = +(page ?? 1);
    const sort = (params.get(SORT) ?? data[DEFAULT_SORT_DATA]).split(',');
    this.predicate = sort[0];
    this.ascending = sort[1] === ASC;
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    this.fillComponentAttributesFromResponseHeader(response.headers);
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.stocks = dataFromBody;
  }

  protected fillComponentAttributesFromResponseBody(data: IStock[] | null): IStock[] {
    return data ?? [];
  }

  protected fillComponentAttributesFromResponseHeader(headers: HttpHeaders): void {
    this.totalItems = Number(headers.get(TOTAL_COUNT_RESPONSE_HEADER));
  }

  protected queryBackend(page?: number, predicate?: string, ascending?: boolean): Observable<EntityArrayResponseType> {
    this.isLoading = true;
    const pageToLoad: number = page ?? 1;
    const queryObject = {
      page: pageToLoad - 1,
      size: this.itemsPerPage,
      eagerload: true,
      sort: this.getSortQueryParam(predicate, ascending),
    };
    return this.stockService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
  }

  protected handleNavigation(page = this.page, predicate?: string, ascending?: boolean): void {
    const queryParamsObj = {
      page,
      size: this.itemsPerPage,
      sort: this.getSortQueryParam(predicate, ascending),
    };

    this.router.navigate(['./'], {
      relativeTo: this.activatedRoute,
      queryParams: queryParamsObj,
    });
  }

  protected getSortQueryParam(predicate = this.predicate, ascending = this.ascending): string[] {
    const ascendingQueryParam = ascending ? ASC : DESC;
    if (predicate === '') {
      return [];
    } else {
      return [predicate + ',' + ascendingQueryParam];
    }
  }
}
