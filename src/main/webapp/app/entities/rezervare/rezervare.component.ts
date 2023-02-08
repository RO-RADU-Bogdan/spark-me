import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable, Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IRezervare } from 'app/shared/model/rezervare.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { RezervareService } from './rezervare.service';
import { RezervareDeleteDialogComponent } from './rezervare-delete-dialog.component';
import { IncarcatorService } from '../incarcator/incarcator.service';
import { StatutRezervare } from 'app/shared/model/enumerations/statut-rezervare.model';
import { faBook } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'jhi-rezervare',
  templateUrl: './rezervare.component.html',
})
export class RezervareComponent implements OnInit, OnDestroy {
  faBook = faBook;

  rezervares: IRezervare[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  // Rezervare user logat curent:
  rezervareCurenta: IRezervare = null;
  isSaving = false;
  //

  constructor(
    protected rezervareService: RezervareService,
    protected incarcatorService: IncarcatorService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.rezervares = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'dataCreare';
    this.ascending = false;
  }

  loadAll(): void {
    this.rezervareService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IRezervare[]>) => this.paginateRezervares(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.rezervares = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInRezervares();
    this.incarcatorService.incarcatorRezervat = null;

    // // Get rezervareCurenta:
    // this.rezervareService.findCurrentRezervare().subscribe(rezervareCurenta => {
    //   this.rezervareCurenta = rezervareCurenta;
    // });
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IRezervare): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  //
  trackStatut(index: number, item: IRezervare): StatutRezervare {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.statut!;
  }

  registerChangeInRezervares(): void {
    this.eventSubscriber = this.eventManager.subscribe('rezervareListModification', () => this.reset());
  }

  delete(rezervare: IRezervare): void {
    const modalRef = this.modalService.open(RezervareDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.rezervare = rezervare;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateRezervares(data: IRezervare[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.rezervares.push(data[i]);
      }
    }
  }

  // Blocare buton confirma:
  verificaIncheiereRezervare(r: IRezervare): Boolean {
    let rezervareIncheiata = false;

    if (r.statut === StatutRezervare.EXPIRAT || r.statut === StatutRezervare.FINALIZAT || r.statut === StatutRezervare.CONFIRMAT) {
      rezervareIncheiata = true;
    }

    return rezervareIncheiata;
  }

  // Confirmare rezervare:
  confirmaRezervare(rezervare: IRezervare): void {
    this.isSaving = true;

    rezervare.statut = StatutRezervare.CONFIRMAT;
    rezervare.dataExpirare = rezervare.dataFinal;
    this.subscribeToSaveResponse(this.rezervareService.updateS(rezervare));
  }

  previousState(): void {
    window.history.back();
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRezervare>>): void {
    result.subscribe(
      () => this.eventManager.broadcast('rezervareListModification'), // actualizeaza lista de rezervari
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    //window.location.reload();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
