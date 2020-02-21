import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SyncifySharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';

@NgModule({
  imports: [SyncifySharedModule, RouterModule.forChild([HOME_ROUTE])],
  declarations: [HomeComponent]
})
export class SyncifyHomeModule {}
