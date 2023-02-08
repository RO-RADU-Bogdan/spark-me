import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ILocatie } from 'app/shared/model/locatie.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { LocatieService } from './locatie.service';
import { LocatieDeleteDialogComponent } from './locatie-delete-dialog.component';

import { faLocationArrow } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'jhi-locatie',
  templateUrl: './locatie.component.html',
})
export class LocatieComponent implements OnInit, OnDestroy {
  faLocationArrow = faLocationArrow;

  locaties: ILocatie[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected locatieService: LocatieService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.locaties = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.locatieService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<ILocatie[]>) => this.paginateLocaties(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.locaties = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInLocaties();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ILocatie): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInLocaties(): void {
    this.eventSubscriber = this.eventManager.subscribe('locatieListModification', () => this.reset());
  }

  delete(locatie: ILocatie): void {
    const modalRef = this.modalService.open(LocatieDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.locatie = locatie;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateLocaties(data: ILocatie[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.locaties.push(data[i]);
      }
    }
  }
}
