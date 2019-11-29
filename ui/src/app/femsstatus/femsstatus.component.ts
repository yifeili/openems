import { Component } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { Edge, Service } from '../shared/shared';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'femsstatus',
  templateUrl: './femsstatus.component.html'
})
export class FemsStatusComponent {

  constructor(
    private translate: TranslateService,
    private route: ActivatedRoute,
    private service: Service,
  ) { }

  ngOnInit() {
    this.service.setCurrentComponent('femsstatus', this.route);
  }

}
