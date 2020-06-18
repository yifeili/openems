import { AbstractHistoryChart } from '../abstracthistorychart';
import { ActivatedRoute } from '@angular/router';
import { Base64PayloadResponse } from 'src/app/shared/jsonrpc/response/base64PayloadResponse';
import { ChannelAddress, Edge, EdgeConfig, Service, Utils, Websocket } from '../../../shared/shared';
import { ChartOptions, Data, DEFAULT_TIME_CHART_OPTIONS, TooltipItem } from './../shared';
import { Component, Input, OnChanges } from '@angular/core';
import { debounceTime, delay, takeUntil } from 'rxjs/operators';
import { DefaultTypes } from 'src/app/shared/service/defaulttypes';
import { EnergyModalComponent } from './modal/modal.component';
import { format, isSameDay, isSameMonth, isSameYear } from 'date-fns';
import { formatNumber } from '@angular/common';
import { fromEvent, Subject } from 'rxjs';
import { ModalController } from '@ionic/angular';
import { QueryHistoricTimeseriesDataResponse } from '../../../shared/jsonrpc/response/queryHistoricTimeseriesDataResponse';
import { QueryHistoricTimeseriesExportXlxsRequest } from 'src/app/shared/jsonrpc/request/queryHistoricTimeseriesExportXlxs';
import { TranslateService } from '@ngx-translate/core';
import * as FileSaver from 'file-saver';

@Component({
  selector: 'energy',
  templateUrl: './energy.component.html'
})
export class EnergyComponent extends AbstractHistoryChart implements OnChanges {

  private static readonly EXCEL_TYPE = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8';
  private static readonly EXCEL_EXTENSION = '.xlsx';

  @Input() private period: DefaultTypes.HistoryPeriod;

  ngOnChanges() {
    this.updateChart();
  };

  constructor(
    protected service: Service,
    protected translate: TranslateService,
    private route: ActivatedRoute,
    public modalCtrl: ModalController,
    private websocket: Websocket,
  ) {
    super(service, translate);
  }

  // EXPORT WILL MOVE TO MODAL WHEN KWH ARE READY

  /**
   * Export historic data to Excel file.
   */
  public exportToXlxs() {
    this.service.getCurrentEdge().then(edge => {
      // TODO the order of these channels should be reflected in the excel file
      let dataChannels = [
        new ChannelAddress('_sum', 'EssActivePower'),
        // Grid
        new ChannelAddress('_sum', 'GridActivePower'),
        // Production
        new ChannelAddress('_sum', 'ProductionActivePower'),
        // Consumption
        new ChannelAddress('_sum', 'ConsumptionActivePower')
      ];
      let energyChannels = [
        // new ChannelAddress('_sum', 'EssSoc'),
        // new ChannelAddress('_sum', 'GridBuyActiveEnergy'),
        // new ChannelAddress('_sum', 'GridSellActiveEnergy'),
        // new ChannelAddress('_sum', 'ProductionActiveEnergy'),
        // new ChannelAddress('_sum', 'ConsumptionActiveEnergy'),
        // new ChannelAddress('_sum', 'EssActiveChargeEnergy'),
        // new ChannelAddress('_sum', 'EssActiveDischargeEnergy')
      ];
      edge.sendRequest(this.websocket, new QueryHistoricTimeseriesExportXlxsRequest(this.service.historyPeriod.from, this.service.historyPeriod.to, dataChannels, energyChannels)).then(response => {
        let r = response as Base64PayloadResponse;
        var binary = atob(r.result.payload.replace(/\s/g, ''));
        var len = binary.length;
        var buffer = new ArrayBuffer(len);
        var view = new Uint8Array(buffer);
        for (var i = 0; i < len; i++) {
          view[i] = binary.charCodeAt(i);
        }
        const data: Blob = new Blob([view], {
          type: EnergyComponent.EXCEL_TYPE
        });

        let fileName = "Export-" + edge.id + "-";
        let dateFrom = this.service.historyPeriod.from;
        let dateTo = this.service.historyPeriod.to;
        if (isSameDay(dateFrom, dateTo)) {
          fileName += format(dateFrom, "dd.MM.yyyy");
        } else if (isSameMonth(dateFrom, dateTo)) {
          fileName += format(dateFrom, "dd.") + "-" + format(dateTo, "dd.MM.yyyy");
        } else if (isSameYear(dateFrom, dateTo)) {
          fileName += format(dateFrom, "dd.MM.") + "-" + format(dateTo, "dd.MM.yyyy");
        } else {
          fileName += format(dateFrom, "dd.MM.yyyy") + "-" + format(dateTo, "dd.MM.yyyy");
        }
        fileName += EnergyComponent.EXCEL_EXTENSION;
        FileSaver.saveAs(data, fileName);

      }).catch(reason => {
        console.warn(reason);
      })
    })
  }

  ngOnInit() {
    this.service.setCurrentComponent('', this.route);
    // Timeout is used to prevent ExpressionChangedAfterItHasBeenCheckedError
    setTimeout(() => this.getChartHeight(), 500);
    this.subscribeChartRefresh()
  }

  ngOnDestroy() {
    this.unsubscribeChartRefresh()
  }

  protected updateChart() {
    this.loading = true;
    this.queryHistoricTimeseriesData(this.period.from, this.period.to).then(response => {
      this.service.getCurrentEdge().then(edge => {
        this.service.getConfig().then(config => {
          let result = (response as QueryHistoricTimeseriesDataResponse).result;

          // convert labels
          let labels: Date[] = [];
          for (let timestamp of result.timestamps) {
            labels.push(new Date(timestamp));
          }
          this.labels = labels;

          // convert datasets
          let datasets = [];

          if (!edge.isVersionAtLeast('2018.8')) {
            this.convertDeprecatedData(config, result.data); // TODO deprecated
          }

          // push data for right y-axis
          if ('_sum/EssSoc' in result.data) {
            let socData = result.data['_sum/EssSoc'].map(value => {
              if (value == null) {
                return null
              } else if (value > 100 || value < 0) {
                return null;
              } else {
                return value;
              }
            })
            datasets.push({
              label: this.translate.instant('General.soc'),
              data: socData,
              hidden: false,
              yAxisID: 'yAxis2',
              position: 'right',
              borderDash: [10, 10]
            })
            this.colors.push({
              backgroundColor: 'rgba(189, 195, 199,0.05)',
              borderColor: 'rgba(189, 195, 199,1)',
            })
          }

          // push data for left y-axis
          if ('_sum/ProductionActivePower' in result.data) {
            /*
            * Production
            */
            let productionData = result.data['_sum/ProductionActivePower'].map(value => {
              if (value == null) {
                return null
              } else {
                return value / 1000; // convert to kW
              }
            });

            datasets.push({
              label: this.translate.instant('General.production'),
              data: productionData,
              hidden: false,
              yAxisID: 'yAxis1',
              position: 'left'
            });
            this.colors.push({
              backgroundColor: 'rgba(45,143,171,0.05)',
              borderColor: 'rgba(45,143,171,1)'
            })
          }

          if ('_sum/GridActivePower' in result.data) {
            /*
             * Buy From Grid
             */
            let buyFromGridData = result.data['_sum/GridActivePower'].map(value => {
              if (value == null) {
                return null
              } else if (value > 0) {
                return value / 1000; // convert to kW
              } else {
                return 0;
              }
            });

            datasets.push({
              label: this.translate.instant('General.gridBuy'),
              data: buyFromGridData,
              hidden: false,
              yAxisID: 'yAxis1',
              position: 'left'
            });
            this.colors.push({
              backgroundColor: 'rgba(0,0,0,0.05)',
              borderColor: 'rgba(0,0,0,1)'
            })

            /*
            * Sell To Grid
            */
            let sellToGridData = result.data['_sum/GridActivePower'].map(value => {
              if (value == null) {
                return null
              } else if (value < 0) {
                return value / -1000; // convert to kW and invert value
              } else {
                return 0;
              }
            });
            datasets.push({
              label: this.translate.instant('General.gridSell'),
              data: sellToGridData,
              hidden: false,
              yAxisID: 'yAxis1',
              position: 'left'
            });
            this.colors.push({
              backgroundColor: 'rgba(0,0,200,0.05)',
              borderColor: 'rgba(0,0,200,1)',
            })
          }

          if ('_sum/ConsumptionActivePower' in result.data) {
            /*
            * Consumption
             */
            let consumptionData = result.data['_sum/ConsumptionActivePower'].map(value => {
              if (value == null) {
                return null
              } else {
                return value / 1000; // convert to kW
              }
            });
            datasets.push({
              label: this.translate.instant('General.consumption'),
              data: consumptionData,
              hidden: false,
              yAxisID: 'yAxis1',
              position: 'left'
            });
            this.colors.push({
              backgroundColor: 'rgba(253,197,7,0.05)',
              borderColor: 'rgba(253,197,7,1)',
            })
          }

          if ('_sum/EssActivePower' in result.data) {
            /*
             * Storage Charge
             */
            let effectivePower;
            if ('_sum/ProductionDcActualPower' in result.data && result.data['_sum/ProductionDcActualPower'].length > 0) {
              effectivePower = result.data['_sum/ProductionDcActualPower'].map((value, index) => {
                return Utils.subtractSafely(result.data['_sum/EssActivePower'][index], value);
              });
            } else {
              effectivePower = result.data['_sum/EssActivePower'];
            }
            let chargeData = effectivePower.map(value => {
              if (value == null) {
                return null
              } else if (value < 0) {
                return value / -1000; // convert to kW;
              } else {
                return 0;
              }
            });
            datasets.push({
              label: this.translate.instant('General.chargePower'),
              data: chargeData,
              hidden: false,
              yAxisID: 'yAxis1',
              position: 'left'
            });
            this.colors.push({
              backgroundColor: 'rgba(0,223,0,0.05)',
              borderColor: 'rgba(0,223,0,1)',
            })
            /*
             * Storage Discharge
             */
            let dischargeData = effectivePower.map(value => {
              if (value == null) {
                return null
              } else if (value > 0) {
                return value / 1000; // convert to kW
              } else {
                return 0;
              }
            });
            datasets.push({
              label: this.translate.instant('General.dischargePower'),
              data: dischargeData,
              hidden: false,
              yAxisID: 'yAxis1',
              position: 'left'
            });
            this.colors.push({
              backgroundColor: 'rgba(200,0,0,0.05)',
              borderColor: 'rgba(200,0,0,1)',
            })
          }
          this.datasets = datasets;
          this.loading = false;
        }).catch(reason => {
          console.error(reason); // TODO error message
          this.initializeChart();
          return;
        });
      }).catch(reason => {
        console.error(reason); // TODO error message
        this.initializeChart();
        return;
      });
    }).catch(reason => {
      console.error(reason); // TODO error message
      this.initializeChart();
      return;
    });
  }

  protected getChannelAddresses(edge: Edge, config: EdgeConfig): Promise<ChannelAddress[]> {
    return new Promise((resolve) => {
      if (edge.isVersionAtLeast('2018.8')) {
        let result: ChannelAddress[] = [];
        config.widgets.classes.forEach(clazz => {
          switch (clazz.toString()) {
            case 'Grid':
              result.push(new ChannelAddress('_sum', 'GridActivePower'));
              break;
            case 'Consumption':
              result.push(new ChannelAddress('_sum', 'ConsumptionActivePower'));
              break;
            case 'Storage':
              result.push(new ChannelAddress('_sum', 'EssSoc'))
              result.push(new ChannelAddress('_sum', 'EssActivePower'));
              break;
            case 'Production':
              result.push(
                new ChannelAddress('_sum', 'ProductionActivePower'),
                new ChannelAddress('_sum', 'ProductionDcActualPower'));
              break;
          };
          return false;
        });
        resolve(result);

      } else {
        this.service.getConfig().then(config => {
          let ignoreIds = config.getComponentIdsImplementingNature("FeneconMiniConsumptionMeter");
          ignoreIds.push.apply(ignoreIds, config.getComponentIdsByFactory("io.openems.impl.device.system.asymmetricsymmetriccombinationess.AsymmetricSymmetricCombinationEssNature"));

          // TODO: remove after full migration
          let result: ChannelAddress[] = [];

          // Ess
          let asymmetricEssChannels = this.getAsymmetric(config.getComponentIdsImplementingNature("AsymmetricEssNature"), ignoreIds);
          if (asymmetricEssChannels.length > 0) {
            // this is an AsymmetricEss Nature
            result.push.apply(result, asymmetricEssChannels);
          } else {
            // this is a SymmetricEss Nature
            result.push.apply(result, this.getSymmetric(config.getComponentIdsImplementingNature("SymmetricEssNature"), ignoreIds));
          }

          // Chargers
          result.push.apply(result, this.getCharger(config.getComponentIdsImplementingNature("ChargerNature"), ignoreIds));

          // Meters
          let asymmetricMeterIds = config.getComponentIdsImplementingNature("AsymmetricMeterNature");
          result.push.apply(result, this.getAsymmetric(asymmetricMeterIds, ignoreIds));
          let symmetricMeterIds = config.getComponentIdsImplementingNature("SymmetricMeterNature").filter(id => !asymmetricMeterIds.includes(id));
          result.push.apply(result, this.getSymmetric(symmetricMeterIds, ignoreIds));

          resolve(result);
        })
      }
    })
  }

  protected setLabel() {
    let translate = this.translate;
    let options = <ChartOptions>Utils.deepCopy(DEFAULT_TIME_CHART_OPTIONS);

    // adds second y-axis to chart
    options.scales.yAxes.push({
      id: 'yAxis2',
      position: 'right',
      scaleLabel: {
        display: true,
        labelString: "%",
        padding: -2,
        fontSize: 11
      },
      gridLines: {
        display: false
      },
      ticks: {
        beginAtZero: true,
        max: 100,
        padding: -5,
        stepSize: 20
      }
    })
    options.scales.yAxes[0].id = "yAxis1"
    options.scales.yAxes[0].scaleLabel.labelString = "kW";
    options.scales.yAxes[0].scaleLabel.padding = -2;
    options.scales.yAxes[0].scaleLabel.fontSize = 11;
    options.scales.yAxes[0].ticks.padding = -5;
    options.tooltips.callbacks.label = function (tooltipItem: TooltipItem, data: Data) {
      let label = data.datasets[tooltipItem.datasetIndex].label;
      let value = tooltipItem.yLabel;
      if (label == translate.instant('General.soc')) {
        return label + ": " + formatNumber(value, 'de', '1.0-0') + " %";
      } else {
        return label + ": " + formatNumber(value, 'de', '1.0-2') + " kW";
      }
    }
    this.options = options;
  }

  private getAsymmetric(ids: string[], ignoreIds: string[]): ChannelAddress[] {
    let result: ChannelAddress[] = [];
    for (let id of ids) {
      if (ignoreIds.includes(id)) {
        continue;
      }
      result.push.apply(result, [
        new ChannelAddress(id, 'ActivePowerL1'),
        new ChannelAddress(id, 'ActivePowerL2'),
        new ChannelAddress(id, 'ActivePowerL3'),
      ]);
    }
    return result;
  }

  private getSymmetric(ids: string[], ignoreIds: string[]): ChannelAddress[] {
    let result: ChannelAddress[] = [];
    for (let id of ids) {
      if (ignoreIds.includes(id)) {
        continue;
      }
      result.push.apply(result, [
        new ChannelAddress(id, 'ActivePower'),
      ]);
    }
    return result;
  }

  private getCharger(ids: string[], ignoreIds: string[]): ChannelAddress[] {
    let result: ChannelAddress[] = [];
    for (let id of ids) {
      if (ignoreIds.includes(id)) {
        continue;
      }
      result.push.apply(result, [
        new ChannelAddress(id, 'ActualPower'),
      ]);
    }
    return result;
  }

  /**
   * Calculates '_sum' values.
   * 
   * @param data 
   */
  private convertDeprecatedData(config: EdgeConfig, data: { [channelAddress: string]: any[] }) {
    let sumEssActivePower = [];
    let sumGridActivePower = [];
    let sumProductionActivePower = [];
    let sumProductionAcActivePower = [];
    let sumProductionDcActualPower = [];

    for (let channel of Object.keys(data)) {
      let channelAddress = ChannelAddress.fromString(channel)
      let componentId = channelAddress.componentId;
      let channelId = channelAddress.channelId;
      let natureIds = config.getNatureIdsByComponentId(componentId);

      if (natureIds.includes('EssNature') && channelId.startsWith('ActivePower')) {
        if (sumEssActivePower.length == 0) {
          sumEssActivePower = data[channel];
        } else {
          sumEssActivePower = data[channel].map((value, index) => {
            return Utils.addSafely(sumEssActivePower[index], value);
          });
        }
      }

      if (natureIds.includes('MeterNature') && channelId.startsWith('ActivePower')) {
        if (componentId === 'meter0') {
          if (sumGridActivePower.length == 0) {
            sumGridActivePower = data[channel];
          } else {
            sumGridActivePower = data[channel].map((value, index) => {
              return Utils.addSafely(sumGridActivePower[index], value);
            });
          }
        } else {
          if (sumProductionActivePower.length == 0) {
            sumProductionActivePower = data[channel];
          } else {
            sumProductionActivePower = data[channel].map((value, index) => {
              return Utils.addSafely(sumProductionActivePower[index], value);
            });
          }
          if (sumProductionAcActivePower.length == 0) {
            sumProductionAcActivePower = data[channel];
          } else {
            sumProductionAcActivePower = data[channel].map((value, index) => {
              return Utils.addSafely(sumProductionAcActivePower[index], value);
            });
          }
        }
      }

      if (natureIds.includes('ChargerNature') && channelId === 'ActualPower') {
        if (sumProductionActivePower.length == 0) {
          sumProductionActivePower = data[channel];
        } else {
          sumProductionActivePower = data[channel].map((value, index) => {
            return Utils.addSafely(sumProductionActivePower[index], value);
          });
        }
        if (sumProductionDcActualPower.length == 0) {
          sumProductionDcActualPower = data[channel];
        } else {
          sumProductionDcActualPower = data[channel].map((value, index) => {
            return Utils.addSafely(sumProductionDcActualPower[index], value);
          });
        }
      }

      data['_sum/EssActivePower'] = sumEssActivePower;
      data['_sum/GridActivePower'] = sumGridActivePower;
      data['_sum/ProductionActivePower'] = sumProductionActivePower;
      data['_sum/ProductionDcActualPower'] = sumProductionDcActualPower;
      data['_sum/ConsumptionActivePower'] = sumEssActivePower.map((ess, index) => {
        return Utils.addSafely(ess, Utils.addSafely(sumProductionAcActivePower[index], sumGridActivePower[index]));
      });
    }
  }

  public getChartHeight(): number {
    return window.innerHeight / 2;
  }

  async presentModal() {
    const modal = await this.modalCtrl.create({
      component: EnergyModalComponent,
    });
    return await modal.present();
  }
}