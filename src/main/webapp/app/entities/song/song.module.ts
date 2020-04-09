import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SyncifySharedModule } from 'app/shared';
import {
  SongComponent,
  SongDetailComponent,
  SongUpdateComponent,
  SongDeletePopupComponent,
  SongDeleteDialogComponent,
  songRoute,
  songPopupRoute
} from './';

const ENTITY_STATES = [...songRoute, ...songPopupRoute];

@NgModule({
  imports: [SyncifySharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [SongComponent, SongDetailComponent, SongUpdateComponent, SongDeleteDialogComponent, SongDeletePopupComponent],
  entryComponents: [SongComponent, SongUpdateComponent, SongDeleteDialogComponent, SongDeletePopupComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SyncifySongModule {}
