import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'song',
        loadChildren: () => import('./song/song.module').then(m => m.SyncifySongModule)
      },
      {
        path: 'playlist',
        loadChildren: () => import('./playlist/playlist.module').then(m => m.SyncifyPlaylistModule)
      },
      {
        path: 'user-details',
        loadChildren: () => import('./user-details/user-details.module').then(m => m.SyncifyUserDetailsModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class SyncifyEntityModule {}
