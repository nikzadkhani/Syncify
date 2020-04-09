import { NgModule } from '@angular/core';

import { SyncifySharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
  imports: [SyncifySharedLibsModule],
  declarations: [JhiAlertComponent, JhiAlertErrorComponent],
  exports: [SyncifySharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class SyncifySharedCommonModule {}
