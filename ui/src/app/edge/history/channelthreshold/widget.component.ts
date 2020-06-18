import { ActivatedRoute } from '@angular/router';
import { calculateActiveTimeOverPeriod } from '../shared';
import { ChannelAddress, Edge, EdgeConfig, Service } from '../../../shared/shared';
import { ChannelthresholdModalComponent } from './modal/modal.component';
import { Component, Input, OnChanges, OnInit } from '@angular/core';
import { DefaultTypes } from 'src/app/shared/service/defaulttypes';
import { ModalController } from '@ionic/angular';
import { QueryHistoricTimeseriesDataResponse } from 'src/app/shared/jsonrpc/response/queryHistoricTimeseriesDataResponse';
import { AbstractHistoryWidget } from '../abstracthistorywidget';

@Component({
    selector: ChanneltresholdWidgetComponent.SELECTOR,
    templateUrl: './widget.component.html'
})
export class ChanneltresholdWidgetComponent extends AbstractHistoryWidget implements OnInit, OnChanges {

    @Input() public period: DefaultTypes.HistoryPeriod;
    @Input() private componentId: string;
    private config: EdgeConfig = null;
    public component: EdgeConfig.Component = null;

    private static readonly SELECTOR = "channelthresholdWidget";

    public activeTimeOverPeriod: string = null;
    public edge: Edge = null;

    constructor(
        public service: Service,
        private route: ActivatedRoute,
        public modalCtrl: ModalController,
    ) {
        super(service);
    }

    ngOnInit() {
        this.service.setCurrentComponent('', this.route).then(response => {
            this.service.getConfig().then(config => {
                this.edge = response;
                this.config = config;
                this.component = config.getComponent(this.componentId);
            })
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
        // Gather result & timestamps to calculate effective active time in % 
        this.queryHistoricTimeseriesData(this.service.historyPeriod.from, this.service.historyPeriod.to).then(response => {
            let result = (response as QueryHistoricTimeseriesDataResponse).result;
            this.service.getConfig().then(config => {
                let outputChannel = ChannelAddress.fromString(config.getComponentProperties(this.componentId)['outputChannelAddress']);
                this.activeTimeOverPeriod = calculateActiveTimeOverPeriod(outputChannel, result);
            })
        });
    };

    protected getChannelAddresses(edge: Edge, config: EdgeConfig): Promise<ChannelAddress[]> {
        return new Promise((resolve) => {
            const outputChannel = ChannelAddress.fromString(config.getComponentProperties(this.componentId)['outputChannelAddress']);
            let channeladdresses = [outputChannel];
            resolve(channeladdresses);
        });
    }

    async presentModal() {
        const modal = await this.modalCtrl.create({
            component: ChannelthresholdModalComponent,
            cssClass: 'wide-modal',
            componentProps: {
                component: this.component,
                config: this.config
            }
        });
        return await modal.present();
    }
}

