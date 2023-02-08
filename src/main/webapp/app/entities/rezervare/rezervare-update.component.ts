import { Component, DoCheck, Input, OnChanges, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IRezervare, Rezervare } from 'app/shared/model/rezervare.model';
import { RezervareService } from './rezervare.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { IIncarcator } from 'app/shared/model/incarcator.model';
import { IncarcatorService } from 'app/entities/incarcator/incarcator.service';
import { StatutRezervare } from 'app/shared/model/enumerations/statut-rezervare.model';
import { Moment } from 'moment';

//type SelectableEntity = IUser | IIncarcator;

@Component({
  selector: 'jhi-rezervare-update',
  templateUrl: './rezervare-update.component.html',
})
export class RezervareUpdateComponent implements OnInit, DoCheck {
  isSaving = false;
  users: IUser[] = [];
  incarcators: IIncarcator[] = [];

  //
  currentUser: IUser = {};
  incarcatorRezervat: IIncarcator = null;
  rezervare: IRezervare = {};

  minDate: Moment = null;
  maxDate: Moment = null;
  maxDurataRezervare: Moment = null;

  minDateString: String = null;
  maxDateString: String = null;
  maxDurataRezervareString: String = null;

  dataStartValida = true;
  dataFinalValida = true;
  durataMaxValida = true;

  listaRezervari: IRezervare[] = null;
  disponibil = true;
  difOre = 0;
  mesajRezervareExistenta: String = null;
  debugIncarcatoareEgale = false;

  editForm = this.fb.group({
    id: [],
    dataCreare: [null, [Validators.required]],
    dataExpirare: [null, [Validators.required]],
    dataStart: [null, [Validators.required]],
    dataFinal: [null, [Validators.required]],
    statut: [null, [Validators.required]],
    user: [],
    incarcator: [],
  });

  constructor(
    protected rezervareService: RezervareService,
    protected userService: UserService,
    protected incarcatorService: IncarcatorService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder,
    private router: Router
  ) {}

  ngDoCheck(): void {
    const rezervare = this.createFromForm();

    this.maxDurataRezervare = moment(rezervare.dataStart).startOf('minute').add(12, 'hours'); // rezervarea se poate face pt. maxim 12h
    this.maxDurataRezervareString = this.maxDurataRezervare.format(DATE_TIME_FORMAT);

    if (rezervare.dataCreare.isAfter(rezervare.dataStart)) {
      this.dataStartValida = false;
    } else this.dataStartValida = true;

    if (rezervare.dataStart.isAfter(rezervare.dataFinal)) {
      this.dataFinalValida = false;
    } else this.dataFinalValida = true;

    if (rezervare.dataFinal.isAfter(this.maxDurataRezervare)) {
      this.durataMaxValida = false;
    } else this.durataMaxValida = true;

    // Verificam daca incarcatorul este disponibil in intervalul de timp selectat:
    if (this.listaRezervari) {
      for (const r of this.listaRezervari) {
        // Daca gasesc rezervare la acelasi incarcator:
        if (
          r.incarcator.id === this.incarcatorRezervat.id &&
          (r.statut === StatutRezervare.CONFIRMAT || r.statut === StatutRezervare.NECONFIRMAT)
        ) {
          this.debugIncarcatoareEgale = true;
          // Compara data si verifica disponibilitate:
          const durataIncarcare = moment.duration(moment(r.dataFinal).diff(moment(r.dataStart)));
          this.difOre = durataIncarcare.asHours(); // intoarce diferenta in ore

          if (this.dataFinalValida) {
            if (
              (rezervare.dataStart.isAfter(moment(r.dataStart).add(this.difOre, 'hours')) &&
                rezervare.dataFinal.isAfter(moment(r.dataFinal))) ||
              (rezervare.dataStart.isBefore(moment(r.dataStart)) && rezervare.dataFinal.isBefore(moment(r.dataStart)))
            ) {
              this.disponibil = true;
            } else {
              this.disponibil = false;
              this.mesajRezervareExistenta = `${moment(r.dataStart).format('DD/MM HH:mm')} - ${moment(r.dataFinal).format('DD/MM HH:mm')}`;
              break;
            }
          }
        }
      }
    }
    //
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rezervare }) => {
      // Verificare statut rezervari:
      this.rezervareService.checkStatutRezervari().subscribe();
      //

      // Create:
      if (!rezervare.id) {
        // Initializare limite de data:
        this.minDate = moment().startOf('minute'); // minDate = today
        this.minDate.format(DATE_TIME_FORMAT);
        this.maxDate = moment().startOf('minute').add(5, 'days'); // max peste 5z
        this.maxDurataRezervare = moment(rezervare.dataStart).startOf('minute').add(12, 'hours'); // rezervarea se poate face pt. maxim 12h

        this.minDateString = this.minDate.format(DATE_TIME_FORMAT);
        this.maxDateString = this.maxDate.format(DATE_TIME_FORMAT);
        this.maxDurataRezervareString = this.maxDurataRezervare.format(DATE_TIME_FORMAT);
        //

        const today = moment().startOf('minute');

        // Expira in 30min:
        // const tmpNow = moment().startOf('minute');
        const expirare = moment().startOf('minute').add(30, 'minutes');
        //

        rezervare.dataCreare = today;
        rezervare.dataExpirare = expirare;
        rezervare.dataStart = today;
        rezervare.dataFinal = expirare;

        rezervare.statut = StatutRezervare.NECONFIRMAT;

        this.incarcatorRezervat = this.incarcatorService.getIncarcatorRezervat();
        rezervare.incarcator = this.incarcatorService.incarcatorRezervat;

        // Primeste user logat curent:
        this.userService.findCurrentUser().subscribe(currentUser => (this.currentUser = currentUser));
        // Salveaza user logat curent in rezervare
        rezervare.user = this.currentUser;

        // Primeste lista rezervarilor:
        this.rezervareService.getListaRezervari().subscribe(listaRezervari => (this.listaRezervari = listaRezervari));
      } else {
        this.rezervare = rezervare;
        this.incarcatorRezervat = this.rezervare.incarcator;
      }

      // Edit:
      this.updateForm(rezervare);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));

      this.incarcatorService
        .query({ filter: 'rezervare-is-null' })
        .pipe(
          map((res: HttpResponse<IIncarcator[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IIncarcator[]) => {
          if (!rezervare.incarcator || !rezervare.incarcator.id) {
            this.incarcators = resBody;
          } else {
            this.incarcatorService
              .find(rezervare.incarcator.id)
              .pipe(
                map((subRes: HttpResponse<IIncarcator>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IIncarcator[]) => (this.incarcators = concatRes));
          }
        });
    });
  }

  updateForm(rezervare: IRezervare): void {
    this.editForm.patchValue({
      id: rezervare.id,
      dataCreare: rezervare.dataCreare ? rezervare.dataCreare.format(DATE_TIME_FORMAT) : null,
      dataExpirare: rezervare.dataExpirare ? rezervare.dataExpirare.format(DATE_TIME_FORMAT) : null,
      dataStart: rezervare.dataStart ? rezervare.dataStart.format(DATE_TIME_FORMAT) : null,
      dataFinal: rezervare.dataFinal ? rezervare.dataFinal.format(DATE_TIME_FORMAT) : null,
      statut: rezervare.statut,
      user: rezervare.user,
      incarcator: rezervare.incarcator,
    });
  }

  previousState(): void {
    window.history.back();
  }

  // Update disponibilitate incarcator la crearea unei rezervari:
  save(): void {
    this.isSaving = true;
    const rezervare = this.createFromForm();

    if (rezervare.user.id === this.currentUser.id) {
      this.rezervareService.userAreRezervare = true;
    }

    if (rezervare.id !== undefined) {
      this.subscribeToSaveResponseIncarcatorRezervat(this.incarcatorService.update(this.incarcatorRezervat));
      this.subscribeToSaveResponse(this.rezervareService.update(rezervare));
    } else {
      this.subscribeToSaveResponseIncarcatorRezervat(this.incarcatorService.update(this.incarcatorRezervat));
      this.subscribeToSaveResponse(this.rezervareService.create(rezervare));
    }
  }

  private createFromForm(): IRezervare {
    return {
      ...new Rezervare(),
      id: this.editForm.get(['id'])!.value,
      dataCreare: this.editForm.get(['dataCreare'])!.value ? moment(this.editForm.get(['dataCreare'])!.value, DATE_TIME_FORMAT) : undefined,
      dataExpirare: this.editForm.get(['dataExpirare'])!.value
        ? moment(this.editForm.get(['dataExpirare'])!.value, DATE_TIME_FORMAT)
        : undefined,
      dataStart: this.editForm.get(['dataStart'])!.value ? moment(this.editForm.get(['dataStart'])!.value, DATE_TIME_FORMAT) : undefined,
      dataFinal: this.editForm.get(['dataFinal'])!.value ? moment(this.editForm.get(['dataFinal'])!.value, DATE_TIME_FORMAT) : undefined,
      statut: this.editForm.get(['statut'])!.value,
      user: this.editForm.get(['user'])!.value,
      incarcator: this.editForm.get(['incarcator'])!.value,
    };
  }

  //
  protected subscribeToSaveResponseIncarcatorRezervat(result: Observable<HttpResponse<IIncarcator>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRezervare>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    //this.previousState();
    this.router.navigate(['rezervare']);
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  // Method overloading in TS:
  trackById(index: number, item: IIncarcator): any;
  trackById(index: number, item: IUser): any;

  trackById(index: number, item: any): any {
    if (item === 'IStatie' || item === 'IUser') return item.id;
  }
  //
}
