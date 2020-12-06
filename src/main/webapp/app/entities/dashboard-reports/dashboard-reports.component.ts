import { Component, OnInit, OnDestroy } from '@angular/core';
import { ChartDataSets } from 'chart.js';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { AuctionService } from '../auction/auction.service';

@Component({
  selector: 'jhi-dashboard-reports',
  templateUrl: './dashboard-reports.component.html',
  styleUrls: ['../../../content/scss/paper-dashboard.scss'],
})
export class DashboardReportsComponent implements OnInit, OnDestroy {
  eventSubscriber?: Subscription;
  data: any;
  labels: any[] = [];
  datasets: ChartDataSets[] = [];

  constructor(protected eventManager: JhiEventManager, protected auctionService: AuctionService) {
    this.loadData();
  }

  loadData(): void {
    const monthsElement = document.querySelector('#monthsFilter') as HTMLSelectElement;
    this.auctionService.getAuctionsWinnerByMonth(+monthsElement.value).subscribe(data => {
      this.data = data.body;
      const months = new Intl.DateTimeFormat('es', { month: 'long' });
      const pointsData: any[] = [];
      for (let i = 0; i < this.data.length; i++) {
        this.labels.push(months.format(new Date(2020, this.data[i].month - 1, 1)));
        pointsData.push(this.data[i].count);
      }
      this.datasets.push({
        label: 'Active Users',
        borderColor: '#f17e5d',
        pointBackgroundColor: '#f17e5d',
        pointRadius: 3,
        pointHoverRadius: 3,
        lineTension: 0,
        fill: false,
        borderWidth: 3,
        data: pointsData,
      });
    });
  }

  ngOnInit(): void {}

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }
}
