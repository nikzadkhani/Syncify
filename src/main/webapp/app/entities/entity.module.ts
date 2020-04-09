import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'song',
        loadChildren: './song/song.module#SyncifySongModule'
      },
      {
        path: 'playlist',
        loadChildren: './playlist/playlist.module#SyncifyPlaylistModule'
      },
      {
        path: 'user-details',
        loadChildren: './user-details/user-details.module#SyncifyUserDetailsModule'
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ],
  declarations: [],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SyncifyEntityModule {}
