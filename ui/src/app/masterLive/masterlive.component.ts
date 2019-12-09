import { Component } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { Edge, Service } from '../shared/shared';
import { ActivatedRoute } from '@angular/router';
import { ClimateModalComponent } from './modal/modalclimate.component';
import { ModalController } from '@ionic/angular';

@Component({
  selector: 'masterlive',
  templateUrl: './masterlive.component.html'
})
export class MasterLiveComponent {

  isLiveChecked: boolean = true;
  isHistoryChecked: boolean = false;

  constructor(
    public translate: TranslateService,
    private route: ActivatedRoute,
    private service: Service,
    public modalCtrl: ModalController,
  ) { }

  ngOnInit() {
    this.service.setCurrentComponent('masterlive', this.route);
  }

  async presentModal() {
    const modal = await this.modalCtrl.create({
      component: ClimateModalComponent,
    });
    return await modal.present();
  }

}
