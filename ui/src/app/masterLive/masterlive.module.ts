import { NgModule } from '@angular/core';
import { SharedModule } from '../shared/shared.module';

import { MasterLiveComponent } from './masterlive.component';

@NgModule({
  imports: [
    SharedModule
  ],
  declarations: [
    MasterLiveComponent,
  ]
})
export class MasterLiveModule { }
