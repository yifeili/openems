import { AbstractHistoryChart } from '../abstracthistorychart';
import { ActivatedRoute } from '@angular/router';
import { ChannelAddress, Edge, EdgeConfig, Service, Utils } from '../../../shared/shared';
import { ChartOptions, Data, DEFAULT_TIME_CHART_OPTIONS, TooltipItem } from '../shared';
import { Component, Input, OnChanges, OnInit } from '@angular/core';
import { DefaultTypes } from 'src/app/shared/service/defaulttypes';
import { formatNumber } from '@angular/common';
import { QueryHistoricTimeseriesDataResponse } from '../../../shared/jsonrpc/response/queryHistoricTimeseriesDataResponse';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'singlethresholdChart',
  templateUrl: '../abstracthistorychart.html'
})
export class SinglethresholdChartComponent extends AbstractHistoryChart implements OnInit, OnChanges {

  @Input() private period: DefaultTypes.HistoryPeriod;
  @Input() public componentId: string;

  ngOnChanges() {
    this.updateChart();
  };

  constructor(
    protected service: Service,
    protected translate: TranslateService,
    private route: ActivatedRoute,
  ) {
    super(service, translate);
  }

  ngOnInit() {
    this.service.setCurrentComponent('', this.route);
    this.subscribeChartRefresh()
  }

  ngOnDestroy() {
    this.unsubscribeChartRefresh()
  }

  protected updateChart() {
    this.colors = [];
    this.loading = true;
    this.queryHistoricTimeseriesData(this.period.from, this.period.to).then(response => {
      this.service.getConfig().then(config => {
        let outputChannel = config.getComponentProperties(this.componentId)['outputChannelAddress'];
        let inputChannel = config.getComponentProperties(this.componentId)['inputChannelAddress'];
        let result = (response as QueryHistoricTimeseriesDataResponse).result;
        let yAxisID

        // set yAxis for % values (if there are no other % values: use left yAxis, if there are: use right yAxis - for percent values)
        if (result.data["_sum/EssSoc"]) {
          yAxisID = "yAxis1";
        } else {
          yAxisID = "yAxis2";
        }

        // convert labels
        let labels: Date[] = [];
        for (let timestamp of result.timestamps) {
          labels.push(new Date(timestamp));
        }
        this.labels = labels;
        let datasets = [];

        // convert datasets
        for (let channel in result.data) {
          if (channel == outputChannel) {
            let address = ChannelAddress.fromString(channel);
            let data = result.data[channel].map(value => {
              if (value == null) {
                return null
              } else {
                return value * 100; // convert to % [0,100]
              }
            });
            datasets.push({
              label: address.channelId,
              data: data,
              hidden: false,
              yAxisID: yAxisID,
              position: 'right'
            });
            this.colors.push({
              backgroundColor: 'rgba(0,191,255,0.05)',
              borderColor: 'rgba(0,191,255,1)',
            })
          }
          if (channel == inputChannel) {
            let inputLabel: string = null;
            let address = ChannelAddress.fromString(channel);
            switch (address.channelId) {
              case 'GridActivePower':
                inputLabel = this.translate.instant('General.grid');
                break;
              case 'ProductionActivePower':
                inputLabel = this.translate.instant('General.production');
                break;
              case 'EssSoc':
                inputLabel = this.translate.instant('General.soc');
                break;
              default:
                inputLabel = this.translate.instant('Edge.Index.Widgets.Singlethreshold.other');
                break;
            }
            let data
            if (address.channelId == 'EssSoc') {
              data = result.data[channel].map(value => {
                if (value == null) {
                  return null
                } else if (value > 100 || value < 0) {
                  return null;
                } else {
                  return value;
                }
              })
            } else if (address.channelId == 'ProductionActivePower' || address.channelId == 'GridActivePower') {
              data = result.data[channel].map(value => {
                if (value == null) {
                  return null
                } else {
                  return value / 1000; // convert to kW
                }
              });
            } else {
              data = result.data[channel].map(value => {
                if (value == null) {
                  return null
                } else {
                  return value;
                }
              });
            }
            if (address.channelId == 'EssSoc') {
              datasets.push({
                label: inputLabel,
                data: data,
                hidden: false,
                yAxisID: yAxisID,
                position: 'right'
              });

              this.colors.push({
                backgroundColor: 'rgba(189, 195, 199,0.05)',
                borderColor: 'rgba(189, 195, 199,1)',
              })
            } else {
              datasets.push({
                label: inputLabel,
                data: data,
                hidden: false,
                yAxisID: 'yAxis1',
                position: 'left',
              });

              this.colors.push({
                backgroundColor: 'rgba(0,0,0,0.05)',
                borderColor: 'rgba(0,0,0,1)'
              })
            }
          }
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
  }

  protected getChannelAddresses(edge: Edge, config: EdgeConfig): Promise<ChannelAddress[]> {
    return new Promise((resolve) => {
      const outputChannel = ChannelAddress.fromString(config.getComponentProperties(this.componentId)['outputChannelAddress']);
      const inputChannel = ChannelAddress.fromString(config.getComponentProperties(this.componentId)['inputChannelAddress']);
      let channeladdresses = [outputChannel, inputChannel];
      resolve(channeladdresses);
    });
  }

  protected setLabel(config: EdgeConfig) {
    let inputChannel = ChannelAddress.fromString(config.getComponentProperties(this.componentId)['inputChannelAddress']);
    let outputChannel = ChannelAddress.fromString(config.getComponentProperties(this.componentId)['outputChannelAddress']);
    let labelString;
    let options = <ChartOptions>Utils.deepCopy(DEFAULT_TIME_CHART_OPTIONS);
    let translate = this.translate;

    if (inputChannel.channelId == 'EssSoc') {
      labelString = '%';
      options.scales.yAxes[0].id = "yAxis1"
      options.scales.yAxes[0].scaleLabel.labelString = labelString;
    } else if (inputChannel.channelId == 'GridActivePower' || inputChannel.channelId == 'ProductionActivePower') {
      labelString = 'kW';
      options.scales.yAxes[0].id = "yAxis1"
      options.scales.yAxes[0].scaleLabel.labelString = labelString;
    } else {
      labelString = config.getChannel(inputChannel)['unit'];
      options.scales.yAxes[0].id = "yAxis1"
      options.scales.yAxes[0].scaleLabel.labelString = labelString;
    }

    if (inputChannel.channelId != 'EssSoc') {
      // adds second y-axis to chart
      options.scales.yAxes.push({
        id: 'yAxis2',
        position: 'right',
        scaleLabel: {
          display: true,
          labelString: "%"
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
    }
    options.tooltips.callbacks.label = function (tooltipItem: TooltipItem, data: Data) {
      let label = data.datasets[tooltipItem.datasetIndex].label;
      let value = tooltipItem.yLabel;
      if (label == outputChannel.channelId || label == translate.instant('General.soc')) {
        return label + ": " + formatNumber(value, 'de', '1.0-0') + " %";
      } else if (label == translate.instant('General.grid') || label == translate.instant('General.production')) {
        return label + ": " + formatNumber(value, 'de', '1.0-2') + " kW";
      } else {
        return label + ": " + formatNumber(value, 'de', '1.0-2') + " " + labelString;
      }
    }
    this.options = options;
  }

  public getChartHeight(): number {
    return window.innerHeight / 1.3;
  }
}