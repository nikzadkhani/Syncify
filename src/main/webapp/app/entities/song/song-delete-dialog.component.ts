import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISong } from 'app/shared/model/song.model';
import { SongService } from './song.service';

@Component({
  templateUrl: './song-delete-dialog.component.html'
})
export class SongDeleteDialogComponent {
  song?: ISong;

  constructor(protected songService: SongService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.songService.delete(id).subscribe(() => {
      this.eventManager.broadcast('songListModification');
      this.activeModal.close();
    });
  }
}
