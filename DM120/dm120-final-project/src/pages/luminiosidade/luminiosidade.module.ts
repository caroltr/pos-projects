import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { LuminiosidadePage } from './luminiosidade';

@NgModule({
  declarations: [
    LuminiosidadePage,
  ],
  imports: [
    IonicPageModule.forChild(LuminiosidadePage),
  ],
})
export class LuminiosidadePageModule {}
