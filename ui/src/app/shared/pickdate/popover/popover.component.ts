import { addDays, getDate, getMonth, getYear, subDays, startOfWeek, endOfWeek } from 'date-fns/esm';
import { Component, Input } from '@angular/core';
import { DefaultTypes } from '../../service/defaulttypes';
import { IAngularMyDpOptions, IMyDate, IMyDateRangeModel, CalAnimation } from 'angular-mydatepicker';
import { isFuture } from 'date-fns';
import { PopoverController } from '@ionic/angular';
import { Service } from '../../shared';
import { TranslateService } from '@ngx-translate/core';


@Component({
    selector: 'pickdatepopover',
    templateUrl: './popover.component.html'
})
export class PickDatePopoverComponent {


    @Input() private setDateRange: (period: DefaultTypes.HistoryPeriod) => void;

    private readonly TODAY = new Date();
    private readonly TOMORROW = addDays(new Date(), 1);

    public locale: string = 'de';
    public showCustomDate: boolean = false;

    myDpOptions: IAngularMyDpOptions = {
        stylesData: {
            selector: 'dp1',
            styles: `
               .dp1 .myDpMarkCurrDay, 
               .dp1 .myDpMarkCurrMonth, 
               .dp1 .myDpMarkCurrYear {
                   border-bottom: 2px solid #2d8fab;
                   color: #2d8fab;
                }
             `
        },
        calendarAnimation: { in: CalAnimation.FlipDiagonal, out: CalAnimation.ScaleCenter },
        dateFormat: 'dd.mm.yyyy',
        dateRange: true,
        disableSince: this.toIMyDate(this.TOMORROW),
        disableUntil: { day: 1, month: 1, year: 2013 }, // TODO start with date since the edge is available
        inline: true,
        selectorHeight: '225px',
        showWeekNumbers: true,
    };

    constructor(
        public service: Service,
        public popoverCtrl: PopoverController,
        public translate: TranslateService,
    ) { }

    ngOnInit() {
        this.locale = this.translate.getBrowserLang();
    }

    /**
     * This is called by the input button on the UI.
     * 
     * @param period
     * @param from
     * @param to
     */
    public setPeriod(period: DefaultTypes.PeriodString) {
        switch (period) {
            case 'day': {
                this.setDateRange(new DefaultTypes.HistoryPeriod(this.TODAY, this.TODAY));
                this.service.periodString = period;
                this.popoverCtrl.dismiss();
                break;
            }
            case 'week': {
                this.setDateRange(new DefaultTypes.HistoryPeriod(startOfWeek(this.TODAY, { weekStartsOn: 1 }), endOfWeek(this.TODAY, { weekStartsOn: 1 })));
                this.service.periodString = period;
                this.popoverCtrl.dismiss();
                break;
            }
        }
    }

    /**
     * Converts a 'Date' to 'IMyDate' format.
     * 
     * @param date the 'Date'
     * @returns the 'IMyDate'
     */
    private toIMyDate(date: Date): IMyDate {
        return { year: getYear(date), month: getMonth(date) + 1, day: getDate(date) }
    }

    public onDateChanged(event: IMyDateRangeModel) {
        this.service.historyPeriod = new DefaultTypes.HistoryPeriod(event.beginJsDate, event.endJsDate);
        this.service.periodString = 'custom';
        this.popoverCtrl.dismiss();
    }
}
