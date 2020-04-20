import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SyncifySharedModule } from 'app/shared/shared.module';
import { PlaylistComponent } from './playlist.component';
import { PlaylistDetailComponent } from './playlist-detail.component';
import { PlaylistUpdateComponent } from './playlist-update.component';
import { PlaylistDeleteDialogComponent } from './playlist-delete-dialog.component';
import { playlistRoute } from './playlist.route';

@NgModule({
  imports: [SyncifySharedModule, RouterModule.forChild(playlistRoute)],
  declarations: [PlaylistComponent, PlaylistDetailComponent, PlaylistUpdateComponent, PlaylistDeleteDialogComponent],
  entryComponents: [PlaylistDeleteDialogComponent]
})
export class SyncifyPlaylistModule {}
