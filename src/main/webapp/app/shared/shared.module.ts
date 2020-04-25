import { NgModule } from '@angular/core';
import { SyncifySharedLibsModule } from './shared-libs.module';
import { AlertComponent } from './alert/alert.component';
import { AlertErrorComponent } from './alert/alert-error.component';
import { HasAnyAuthorityDirective } from './auth/has-any-authority.directive';

@NgModule({
  imports: [SyncifySharedLibsModule],
  declarations: [AlertComponent, AlertErrorComponent, HasAnyAuthorityDirective],
  exports: [SyncifySharedLibsModule, AlertComponent, AlertErrorComponent, HasAnyAuthorityDirective]
})
export class SyncifySharedModule {}
