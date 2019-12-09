import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Edge, Service, Utils, EdgeConfig } from '../../shared/shared';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'settings',
  templateUrl: './settings.component.html'
})
export class SettingsComponent implements OnInit {

  public edge: Edge = null;
  public isSoltaroEdge: boolean = null;

  constructor(
    private route: ActivatedRoute,
    protected utils: Utils,
    private service: Service,
    private translate: TranslateService
  ) {
  }

  ngOnInit() {
    this.service.setCurrentComponent(this.translate.instant('Menu.EdgeSettings'), this.route).then(edge => {
      this.edge = edge
    });
    this.service.getConfig().then(config => {
      if (config.getComponentIdsByFactory('Bms.Soltaro.Cluster.VersionB').length == 0 && config.getComponentIdsByFactory('Bms.Soltaro.SingleRack.VersionA').length == 0
        && config.getComponentIdsByFactory('Bms.Soltaro.SingleRack.VersionB').length == 0) {
        this.isSoltaroEdge = false;
      } else {
        this.isSoltaroEdge = true;
      }
    })
  }

}