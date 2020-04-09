import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IPlaylist } from 'app/shared/model/playlist.model';
import { AccountService } from 'app/core';
import { PlaylistService } from './playlist.service';

@Component({
  selector: 'jhi-playlist',
  templateUrl: './playlist.component.html'
})
export class PlaylistComponent implements OnInit, OnDestroy {
  playlists: IPlaylist[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected playlistService: PlaylistService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.playlistService
      .query()
      .pipe(
        filter((res: HttpResponse<IPlaylist[]>) => res.ok),
        map((res: HttpResponse<IPlaylist[]>) => res.body)
      )
      .subscribe(
        (res: IPlaylist[]) => {
          this.playlists = res;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInPlaylists();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IPlaylist) {
    return item.id;
  }

  registerChangeInPlaylists() {
    this.eventSubscriber = this.eventManager.subscribe('playlistListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
