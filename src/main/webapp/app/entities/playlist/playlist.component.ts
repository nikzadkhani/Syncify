import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPlaylist } from 'app/shared/model/playlist.model';
import { PlaylistService } from './playlist.service';
import { PlaylistDeleteDialogComponent } from './playlist-delete-dialog.component';

@Component({
  selector: 'jhi-playlist',
  templateUrl: './playlist.component.html'
})
export class PlaylistComponent implements OnInit, OnDestroy {
  playlists?: IPlaylist[];
  eventSubscriber?: Subscription;

  constructor(protected playlistService: PlaylistService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.playlistService.query().subscribe((res: HttpResponse<IPlaylist[]>) => (this.playlists = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInPlaylists();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IPlaylist): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInPlaylists(): void {
    this.eventSubscriber = this.eventManager.subscribe('playlistListModification', () => this.loadAll());
  }

  delete(playlist: IPlaylist): void {
    const modalRef = this.modalService.open(PlaylistDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.playlist = playlist;
  }
}
