import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISong } from 'app/shared/model/song.model';
import { SongService } from './song.service';
import { SongDeleteDialogComponent } from './song-delete-dialog.component';

@Component({
  selector: 'jhi-song',
  templateUrl: './song.component.html'
})
export class SongComponent implements OnInit, OnDestroy {
  songs?: ISong[];
  eventSubscriber?: Subscription;

  constructor(protected songService: SongService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.songService.query().subscribe((res: HttpResponse<ISong[]>) => (this.songs = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInSongs();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ISong): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInSongs(): void {
    this.eventSubscriber = this.eventManager.subscribe('songListModification', () => this.loadAll());
  }

  delete(song: ISong): void {
    const modalRef = this.modalService.open(SongDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.song = song;
  }
}
