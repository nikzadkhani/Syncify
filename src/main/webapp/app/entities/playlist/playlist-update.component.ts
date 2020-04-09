import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IPlaylist, Playlist } from 'app/shared/model/playlist.model';
import { PlaylistService } from './playlist.service';
import { ISong } from 'app/shared/model/song.model';
import { SongService } from 'app/entities/song';
import { IUserDetails } from 'app/shared/model/user-details.model';
import { UserDetailsService } from 'app/entities/user-details';

@Component({
  selector: 'jhi-playlist-update',
  templateUrl: './playlist-update.component.html'
})
export class PlaylistUpdateComponent implements OnInit {
  isSaving: boolean;

  songs: ISong[];

  userdetails: IUserDetails[];

  editForm = this.fb.group({
    id: [],
    name: [],
    author: [],
    song: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected playlistService: PlaylistService,
    protected songService: SongService,
    protected userDetailsService: UserDetailsService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ playlist }) => {
      this.updateForm(playlist);
    });
    this.songService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ISong[]>) => mayBeOk.ok),
        map((response: HttpResponse<ISong[]>) => response.body)
      )
      .subscribe((res: ISong[]) => (this.songs = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.userDetailsService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IUserDetails[]>) => mayBeOk.ok),
        map((response: HttpResponse<IUserDetails[]>) => response.body)
      )
      .subscribe((res: IUserDetails[]) => (this.userdetails = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(playlist: IPlaylist) {
    this.editForm.patchValue({
      id: playlist.id,
      name: playlist.name,
      author: playlist.author,
      song: playlist.song
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
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
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      author: this.editForm.get(['author']).value,
      song: this.editForm.get(['song']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlaylist>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackSongById(index: number, item: ISong) {
    return item.id;
  }

  trackUserDetailsById(index: number, item: IUserDetails) {
    return item.id;
  }

  getSelected(selectedVals: Array<any>, option: any) {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
