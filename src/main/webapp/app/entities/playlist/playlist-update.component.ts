import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IPlaylist, Playlist } from 'app/shared/model/playlist.model';
import { PlaylistService } from './playlist.service';
import { ISong } from 'app/shared/model/song.model';
import { SongService } from 'app/entities/song/song.service';

@Component({
  selector: 'jhi-playlist-update',
  templateUrl: './playlist-update.component.html'
})
export class PlaylistUpdateComponent implements OnInit {
  isSaving = false;
  songs: ISong[] = [];

  editForm = this.fb.group({
    id: [],
    syncifyId: [],
    name: [],
    author: [],
    songId: []
  });

  constructor(
    protected playlistService: PlaylistService,
    protected songService: SongService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ playlist }) => {
      this.updateForm(playlist);

      this.songService.query().subscribe((res: HttpResponse<ISong[]>) => (this.songs = res.body || []));
    });
  }

  updateForm(playlist: IPlaylist): void {
    this.editForm.patchValue({
      id: playlist.id,
      syncifyId: playlist.syncifyId,
      name: playlist.name,
      author: playlist.author,
      songId: playlist.songId
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const playlist = this.createFromForm();
    if (playlist.id !== undefined) {
      this.subscribeToSaveResponse(this.playlistService.update(playlist));
    } else {
      this.subscribeToSaveResponse(this.playlistService.create(playlist));
    }
  }

  private createFromForm(): IPlaylist {
    return {
      ...new Playlist(),
      id: this.editForm.get(['id'])!.value,
      syncifyId: this.editForm.get(['syncifyId'])!.value,
      name: this.editForm.get(['name'])!.value,
      author: this.editForm.get(['author'])!.value,
      songId: this.editForm.get(['songId'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlaylist>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: ISong): any {
    return item.id;
  }
}
