import { Component } from '@angular/core';
import { ModalController } from '@ionic/angular';
import { Service } from 'src/app/shared/shared';

@Component({
  selector: ClimateModalComponent.SELECTOR,
  templateUrl: './modalclimate.component.html'
})
export class ClimateModalComponent {

  private static readonly SELECTOR = "climate-modal";

  constructor(
    public service: Service,
    public modalCtrl: ModalController,
  ) { }
}