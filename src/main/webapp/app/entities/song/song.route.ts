import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ISong, Song } from 'app/shared/model/song.model';
import { SongService } from './song.service';
import { SongComponent } from './song.component';
import { SongDetailComponent } from './song-detail.component';
import { SongUpdateComponent } from './song-update.component';

@Injectable({ providedIn: 'root' })
export class SongResolve implements Resolve<ISong> {
  constructor(private service: SongService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISong> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((song: HttpResponse<Song>) => {
          if (song.body) {
            return of(song.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Song());
  }
}

export const songRoute: Routes = [
  {
    path: '',
    component: SongComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Songs'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: SongDetailComponent,
    resolve: {
      song: SongResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Songs'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: SongUpdateComponent,
    resolve: {
      song: SongResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Songs'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: SongUpdateComponent,
    resolve: {
      song: SongResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Songs'
    },
    canActivate: [UserRouteAccessService]
  }
];
