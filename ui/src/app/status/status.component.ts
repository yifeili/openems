import { Component } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { Edge, Service } from '../shared/shared';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'status',
  templateUrl: './status.component.html'
})
export class StatusComponent {

  constructor(
    private translate: TranslateService,
    private route: ActivatedRoute,
    private service: Service,
  ) { }

  ngOnInit() {
    this.service.setCurrentComponent('status', this.route);
  }

}
