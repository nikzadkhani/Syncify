import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SyncifySharedModule } from 'app/shared';
import {
  PlaylistComponent,
  PlaylistDetailComponent,
  PlaylistUpdateComponent,
  PlaylistDeletePopupComponent,
  PlaylistDeleteDialogComponent,
  playlistRoute,
  playlistPopupRoute
} from './';

const ENTITY_STATES = [...playlistRoute, ...playlistPopupRoute];

@NgModule({
  imports: [SyncifySharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    PlaylistComponent,
    PlaylistDetailComponent,
    PlaylistUpdateComponent,
    PlaylistDeleteDialogComponent,
    PlaylistDeletePopupComponent
  ],
  entryComponents: [PlaylistComponent, PlaylistUpdateComponent, PlaylistDeleteDialogComponent, PlaylistDeletePopupComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SyncifyPlaylistModule {}
