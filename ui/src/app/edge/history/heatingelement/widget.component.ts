import { ActivatedRoute } from '@angular/router';
import { ChannelAddress, Edge, EdgeConfig, Service } from '../../../shared/shared';
import { Component, Input, OnChanges, OnInit } from '@angular/core';
import { DefaultTypes } from 'src/app/shared/service/defaulttypes';
import { HeatingelementModalComponent } from './modal/modal.component';
import { ModalController } from '@ionic/angular';
import { QueryHistoricTimeseriesDataResponse } from 'src/app/shared/jsonrpc/response/queryHistoricTimeseriesDataResponse';
import { AbstractHistoryWidget } from '../abstracthistorywidget';

@Component({
    selector: HeatingelementWidgetComponent.SELECTOR,
    templateUrl: './widget.component.html'
})
export class HeatingelementWidgetComponent extends AbstractHistoryWidget implements OnInit, OnChanges {

    @Input() public period: DefaultTypes.HistoryPeriod;
    @Input() private componentId: string;

    private static readonly SELECTOR = "heatingelementWidget";

    public component: EdgeConfig.Component = null;

    public activeTimeOverPeriodLevel1: number = null;
    public activeTimeOverPeriodLevel2: number = null;
    public activeTimeOverPeriodLevel3: number = null;

    public edge: Edge = null;

    constructor(
        public modalCtrl: ModalController,
        public service: Service,
        private route: ActivatedRoute,
    ) {
        super(service);
    }

    ngOnInit() {
        this.service.setCurrentComponent('', this.route).then(edge => {
            this.edge = edge;
            this.service.getConfig().then(config => {
                this.component = config.getComponent(this.componentId);
            });
        });
        this.subscribeWidgetRefresh()
    }

    ngOnDestroy() {
        this.unsubscribeWidgetRefresh()
    }

    ngOnChanges() {
        this.updateValues();
    };

    protected updateValues() {
        this.queryHistoricTimeseriesData(this.service.historyPeriod.from, this.service.historyPeriod.to).then(response => {
            let result = (response as QueryHistoricTimeseriesDataResponse).result;

            let level1Time = result.data[this.componentId + '/Level1Time'];
            let level2Time = result.data[this.componentId + '/Level2Time'];
            let level3Time = result.data[this.componentId + '/Level3Time'];

            let lastValueLevel1 = null;
            let lastValueLevel2 = null;
            let lastValueLevel3 = null;

            let sumLevel1 = 0;
            let sumLevel2 = 0;
            let sumLevel3 = 0;

            for (let value of level1Time.slice().reverse()) {
                if (value == null) {
                    continue;
                }
                if (lastValueLevel1 == null || value > lastValueLevel1) {
                    sumLevel1 += value;
                }
                lastValueLevel1 = value;
            }
            this.activeTimeOverPeriodLevel1 = sumLevel1;

            for (let value of level2Time.slice().reverse()) {
                if (value == null) {
                    continue;
                }
                if (lastValueLevel2 == null || value > lastValueLevel2) {
                    sumLevel2 += value;
                }
                lastValueLevel2 = value;
            }
            this.activeTimeOverPeriodLevel2 = sumLevel2;

            for (let value of level3Time.slice().reverse()) {
                if (value == null) {
                    continue;
                }
                if (lastValueLevel3 == null || value > lastValueLevel3) {
                    sumLevel3 += value;
                }
                lastValueLevel3 = value;
            }
            this.activeTimeOverPeriodLevel3 = sumLevel3;
        });
    };

    protected getChannelAddresses(edge: Edge, config: EdgeConfig): Promise<ChannelAddress[]> {
        return new Promise((resolve) => {
            let channeladdresses = [
                new ChannelAddress(this.componentId, 'Level1Time'),
                new ChannelAddress(this.componentId, 'Level2Time'),
                new ChannelAddress(this.componentId, 'Level3Time'),
            ];
            resolve(channeladdresses);
        });
    }

    async presentModal() {
        const modal = await this.modalCtrl.create({
            component: HeatingelementModalComponent,
            cssClass: 'wide-modal',
            componentProps: {
                component: this.component,
            }
        });
        return await modal.present();
    }
}