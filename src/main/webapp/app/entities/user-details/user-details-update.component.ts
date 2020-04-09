import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IUserDetails, UserDetails } from 'app/shared/model/user-details.model';
import { UserDetailsService } from './user-details.service';
import { IPlaylist } from 'app/shared/model/playlist.model';
import { PlaylistService } from 'app/entities/playlist';

@Component({
  selector: 'jhi-user-details-update',
  templateUrl: './user-details-update.component.html'
})
export class UserDetailsUpdateComponent implements OnInit {
  isSaving: boolean;

  playlists: IPlaylist[];

  editForm = this.fb.group({
    id: [],
    platformUserName: [],
    playlists: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected userDetailsService: UserDetailsService,
    protected playlistService: PlaylistService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ userDetails }) => {
      this.updateForm(userDetails);
    });
    this.playlistService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IPlaylist[]>) => mayBeOk.ok),
        map((response: HttpResponse<IPlaylist[]>) => response.body)
      )
      .subscribe((res: IPlaylist[]) => (this.playlists = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(userDetails: IUserDetails) {
    this.editForm.patchValue({
      id: userDetails.id,
      platformUserName: userDetails.platformUserName,
      playlists: userDetails.playlists
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const userDetails = this.createFromForm();
    if (userDetails.id !== undefined) {
      this.subscribeToSaveResponse(this.userDetailsService.update(userDetails));
    } else {
      this.subscribeToSaveResponse(this.userDetailsService.create(userDetails));
    }
  }

  private createFromForm(): IUserDetails {
    return {
      ...new UserDetails(),
      id: this.editForm.get(['id']).value,
      platformUserName: this.editForm.get(['platformUserName']).value,
      playlists: this.editForm.get(['playlists']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserDetails>>) {
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

  trackPlaylistById(index: number, item: IPlaylist) {
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
