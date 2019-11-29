import { NgModule } from '@angular/core';
import { SharedModule } from '../shared/shared.module';

import { StatusComponent } from './status.component';

@NgModule({
  imports: [
    SharedModule
  ],
  declarations: [
    StatusComponent,
  ]
})
export class StatusModule { }