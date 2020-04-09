import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPlaylist } from 'app/shared/model/playlist.model';

@Component({
  selector: 'jhi-playlist-detail',
  templateUrl: './playlist-detail.component.html'
})
export class PlaylistDetailComponent implements OnInit {
  playlist: IPlaylist;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ playlist }) => {
      this.playlist = playlist;
    });
  }

  previousState() {
    window.history.back();
  }
}
