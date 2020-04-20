import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IUserDetails, UserDetails } from 'app/shared/model/user-details.model';
import { UserDetailsService } from './user-details.service';
import { IPlaylist } from 'app/shared/model/playlist.model';
import { PlaylistService } from 'app/entities/playlist/playlist.service';

@Component({
  selector: 'jhi-user-details-update',
  templateUrl: './user-details-update.component.html'
})
export class UserDetailsUpdateComponent implements OnInit {
  isSaving = false;
  playlists: IPlaylist[] = [];

  editForm = this.fb.group({
    id: [],
    syncifyId: [],
    platformUserName: [],
    playlistIds: []
  });

  constructor(
    protected userDetailsService: UserDetailsService,
    protected playlistService: PlaylistService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userDetails }) => {
      this.updateForm(userDetails);

      this.playlistService.query().subscribe((res: HttpResponse<IPlaylist[]>) => (this.playlists = res.body || []));
    });
  }

  updateForm(userDetails: IUserDetails): void {
    this.editForm.patchValue({
      id: userDetails.id,
      syncifyId: userDetails.syncifyId,
      platformUserName: userDetails.platformUserName,
      playlistIds: userDetails.playlistIds
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
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
      id: this.editForm.get(['id'])!.value,
      syncifyId: this.editForm.get(['syncifyId'])!.value,
      platformUserName: this.editForm.get(['platformUserName'])!.value,
      playlistIds: this.editForm.get(['playlistIds'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserDetails>>): void {
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

  trackById(index: number, item: IPlaylist): any {
    return item.id;
  }

  getSelected(selectedVals: IPlaylist[], option: IPlaylist): IPlaylist {
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
