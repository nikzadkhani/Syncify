import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SyncifySharedModule } from 'app/shared/shared.module';
import { UserDetailsComponent } from './user-details.component';
import { UserDetailsDetailComponent } from './user-details-detail.component';
import { UserDetailsUpdateComponent } from './user-details-update.component';
import { UserDetailsDeleteDialogComponent } from './user-details-delete-dialog.component';
import { userDetailsRoute } from './user-details.route';

@NgModule({
  imports: [SyncifySharedModule, RouterModule.forChild(userDetailsRoute)],
  declarations: [UserDetailsComponent, UserDetailsDetailComponent, UserDetailsUpdateComponent, UserDetailsDeleteDialogComponent],
  entryComponents: [UserDetailsDeleteDialogComponent]
})
export class SyncifyUserDetailsModule {}
