import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISong } from 'app/shared/model/song.model';

@Component({
  selector: 'jhi-song-detail',
  templateUrl: './song-detail.component.html'
})
export class SongDetailComponent implements OnInit {
  song: ISong;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ song }) => {
      this.song = song;
    });
  }

  previousState() {
    window.history.back();
  }
}
