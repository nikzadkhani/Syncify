import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { SyncifySharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';

@NgModule({
  imports: [SyncifySharedCommonModule],
  declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective],
  entryComponents: [JhiLoginModalComponent],
  exports: [SyncifySharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SyncifySharedModule {
  static forRoot() {
    return {
      ngModule: SyncifySharedModule
    };
  }
}
