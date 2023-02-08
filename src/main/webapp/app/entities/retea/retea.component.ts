import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IRetea } from 'app/shared/model/retea.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { ReteaService } from './retea.service';
import { ReteaDeleteDialogComponent } from './retea-delete-dialog.component';
import { faUsers } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'jhi-retea',
  templateUrl: './retea.component.html',
})
export class ReteaComponent implements OnInit, OnDestroy {
  faUsers = faUsers;

  reteas: IRetea[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected reteaService: ReteaService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.reteas = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.reteaService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IRetea[]>) => this.paginateReteas(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.reteas = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInReteas();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IRetea): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInReteas(): void {
    this.eventSubscriber = this.eventManager.subscribe('reteaListModification', () => this.reset());
  }

  delete(retea: IRetea): void {
    const modalRef = this.modalService.open(ReteaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.retea = retea;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateReteas(data: IRetea[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.reteas.push(data[i]);
      }
    }
  }
}
