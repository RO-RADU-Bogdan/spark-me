import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IIncarcator } from 'app/shared/model/incarcator.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { IncarcatorService } from './incarcator.service';
import { IncarcatorDeleteDialogComponent } from './incarcator-delete-dialog.component';

import { faBatteryThreeQuarters } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'jhi-incarcator',
  templateUrl: './incarcator.component.html',
})
export class IncarcatorComponent implements OnInit, OnDestroy {
  faBatteryThreeQuarters = faBatteryThreeQuarters;

  incarcators: IIncarcator[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected incarcatorService: IncarcatorService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.incarcators = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.incarcatorService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IIncarcator[]>) => this.paginateIncarcators(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.incarcators = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInIncarcators();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IIncarcator): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInIncarcators(): void {
    this.eventSubscriber = this.eventManager.subscribe('incarcatorListModification', () => this.reset());
  }

  delete(incarcator: IIncarcator): void {
    const modalRef = this.modalService.open(IncarcatorDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.incarcator = incarcator;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateIncarcators(data: IIncarcator[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.incarcators.push(data[i]);
      }
    }
  }
}
