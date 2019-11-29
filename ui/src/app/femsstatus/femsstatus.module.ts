import { NgModule } from '@angular/core';
import { SharedModule } from '../shared/shared.module';
import { FemsStatusComponent } from './femsstatus.component';


@NgModule({
  imports: [
    SharedModule
  ],
  declarations: [
    FemsStatusComponent,
  ]
})
export class FemsStatusModule { }