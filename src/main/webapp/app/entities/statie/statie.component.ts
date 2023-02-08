import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IStatie } from 'app/shared/model/statie.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { StatieService } from './statie.service';
import { StatieDeleteDialogComponent } from './statie-delete-dialog.component';
import { faChargingStation } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'jhi-statie',
  templateUrl: './statie.component.html',
})
export class StatieComponent implements OnInit, OnDestroy {
  faChargingStation = faChargingStation;
  
  staties: IStatie[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected statieService: StatieService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.staties = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.statieService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IStatie[]>) => this.paginateStaties(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.staties = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInStaties();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IStatie): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInStaties(): void {
    this.eventSubscriber = this.eventManager.subscribe('statieListModification', () => this.reset());
  }

  delete(statie: IStatie): void {
    const modalRef = this.modalService.open(StatieDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.statie = statie;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateStaties(data: IStatie[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.staties.push(data[i]);
      }
    }
  }
}
