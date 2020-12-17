import { Component, OnInit, OnDestroy } from '@angular/core';
import { ChartDataSets } from 'chart.js';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { AuctionService } from '../auction/auction.service';
import { RaffleService } from '../raffle/raffle.service';
import { ProyectService } from '../proyect/proyect.service';
import { IProyect } from 'app/shared/model/proyect.model';
import { HttpResponse } from '@angular/common/http';
import { Authority } from 'app/shared/constants/authority.constants';
import { AccountService } from '../../core/auth/account.service';
import { FavoriteService } from '../favorite/favorite.service';

@Component({
  selector: 'jhi-dashboard-reports',
  templateUrl: './dashboard-reports.component.html',
  styleUrls: ['../../../content/scss/paper-dashboard.scss', './dashboard-reports.component.scss'],
})
export class DashboardReportsComponent implements OnInit, OnDestroy {
  eventSubscriber?: Subscription;
  data: any;
  dataRaffle: any;
  labels: any[] = [];
  datasets: ChartDataSets[] = [];
  labelsRaffle: any[] = [];
  datasetsRaffle: ChartDataSets[] = [];
  chart: any;
  chartRaffle: any;
  raffleChartConfig = [
    {
      color: '#f17e5d',
      title: 'Rifas participadas',
    },
    {
      color: '#4cbdd7',
      title: 'Rifas ganadas',
    },
  ];
  activeProyects: IProyect[] | undefined;
  inactiveProyects: IProyect[] | undefined;
  totalActive!: number;
  totalInactive!: number;
  dataProyectStatus: ChartDataSets[] = [];
  labelsProyectStatus: any[] = [];
  dataProyectReport: any;
  dataProyectComplete: ChartDataSets[] = [];
  labelsProyectComplete: any[] = [];
  dataProyectReportComplete: any;
  totalComplete = 0;
  totalIncomplete = 0;
  dataFollowersReport: any;
  labelsFollowers: any[] = [];
  dataFollowers: ChartDataSets[] = [];
  dataProyectCategoryReport: any;
  dataProyectCategory: ChartDataSets[] = [];
  labelsProyectCategory: any[] = [];
  constructor(
    protected eventManager: JhiEventManager,
    protected auctionService: AuctionService,
    protected raffleService: RaffleService,
    protected proyectService: ProyectService,
    protected accountService: AccountService,
    protected favoriteService: FavoriteService
  ) {
    this.loadData();
    this.loadDataRaffle();
    this.loadProjects();
    this.loadProyectReportStatus();
    this.loadProyectReportCompletePercentile();
    this.loadMostFollowersProject();
    this.loadProyectCategoryReport();
  }

  loadData(): void {
    const monthsElement = document.querySelector('#monthsFilter') as HTMLSelectElement;
    const number = monthsElement ? +monthsElement.value : 6;
    this.auctionService.getAuctionsWinnerByMonth(number).subscribe(data => {
      this.data = data.body;
      const months = new Intl.DateTimeFormat('es', { month: 'long' });
      const pointsData: any[] = [];
      this.labels = [];
      this.datasets = [];
      for (let i = 0; i < this.data.length; i++) {
        this.labels.push(months.format(new Date(2020, this.data[i].month - 1, 1)));
        pointsData.push(this.data[i].count);
      }
      this.datasets.push({
        label: 'Subastas',
        borderColor: '#6bd098',
        pointBackgroundColor: '#6bd098',
        pointRadius: 3,
        pointHoverRadius: 3,
        lineTension: 0,
        fill: false,
        borderWidth: 3,
        data: pointsData,
      });
      if (this.chart) {
        this.chart.data.labels = this.labels;
        this.chart.data.datasets = this.datasets;
        this.chart.update();
      }
    });
  }

  loadDataRaffle(): void {
    const monthsElement = document.querySelector('#monthsRaffle') as HTMLSelectElement;
    const number = monthsElement ? +monthsElement.value : 6;
    this.raffleService.getDataReportRaffle(number).subscribe(data => {
      this.dataRaffle = data.body;
      const months = new Intl.DateTimeFormat('es', { month: 'long' });
      let pointsData: any[] = [];
      this.labelsRaffle = [];
      this.datasetsRaffle = [];
      for (let i = 0; i < this.dataRaffle[0].length; i++) {
        this.labelsRaffle.push(months.format(new Date(2020, this.dataRaffle[0][i].month - 1, 1)));
      }
      const datachart = [];
      for (let i = 0; i < this.dataRaffle.length; i++) {
        for (let j = 0; j < this.dataRaffle[i].length; j++) {
          const element = this.dataRaffle[i][j];
          pointsData.push(element.count);
        }
        datachart.push({
          label: this.raffleChartConfig[i].title,
          borderColor: this.raffleChartConfig[i].color,
          pointBackgroundColor: this.raffleChartConfig[i].color,
          pointRadius: 3,
          pointHoverRadius: 3,
          lineTension: 0,
          fill: false,
          borderWidth: 3,
          data: pointsData,
        });
        pointsData = [];
      }
      this.datasetsRaffle = datachart;
      if (this.chartRaffle) {
        this.chartRaffle.data.labels = this.labelsRaffle;
        this.chartRaffle.data.datasets = this.datasetsRaffle;
        this.chartRaffle.update();
      }
    });
  }

  loadProyectReportStatus(): void {
    this.proyectService.getReportStatus().subscribe(data => {
      this.dataProyectReport = data.body;
      const dataResult = [];
      for (let i = 0; i < this.dataProyectReport.length; i++) {
        if (this.dataProyectReport[i].status) {
          this.labelsProyectStatus.push('Activado');
        } else {
          this.labelsProyectStatus.push('Desactivado');
        }
        dataResult.push(this.dataProyectReport[i].count);
      }
      this.dataProyectStatus.push({
        label: 'Proyect Report',
        pointRadius: 0,
        pointHoverRadius: 0,
        backgroundColor: ['#fcc468', '#4acccd'],
        borderWidth: 0,
        data: dataResult,
      });
    });
  }

  loadProyectReportCompletePercentile(): void {
    this.proyectService.getCompletePercentile().subscribe(data => {
      this.dataProyectReportComplete = data.body;
      const dataResult = [];
      for (let i = 0; i < this.dataProyectReportComplete.length; i++) {
        this.labelsProyectComplete.push(this.dataProyectReportComplete[i].name);
        dataResult.push(this.dataProyectReportComplete[i].complete.toFixed(2));
      }
      this.dataProyectComplete.push({
        label: 'Data',
        borderColor: '#fcc468',
        fill: true,
        backgroundColor: '#fcc468',
        hoverBorderColor: '#fcc468',
        borderWidth: 5,
        data: dataResult,
      });
    });
  }

  loadProyectCategoryReport(): void {
    this.proyectService.getCategoryReport().subscribe(data => {
      this.dataProyectCategoryReport = data.body;
      const dataResult = [];
      for (let i = 0; i < this.dataProyectCategoryReport.length; i++) {
        this.labelsProyectCategory.push(this.dataProyectCategoryReport[i].name);
        dataResult.push(this.dataProyectCategoryReport[i].count);
      }
      this.dataProyectCategory.push({
        label: 'Cantidad',
        borderColor: '#fcc468',
        fill: true,
        backgroundColor: '#fcc468',
        hoverBorderColor: '#fcc468',
        borderWidth: 5,
        data: dataResult,
      });
    });
  }

  ngOnInit(): void {}

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  onchange(chart: any): void {
    this.chart = chart;
  }

  onchangeRaffle(chart: any): void {
    this.chartRaffle = chart;
  }

  loadProjects(): void {
    this.proyectService.query({ 'status.equals': true }).subscribe((res: HttpResponse<IProyect[]>) => {
      this.activeProyects = res.body || [];
      this.totalActive = this.activeProyects.length;
      this.checkGoalActive();
    });

    this.proyectService.query({ 'status.equals': false }).subscribe((res: HttpResponse<IProyect[]>) => {
      this.inactiveProyects = res.body || [];
      this.totalInactive = this.inactiveProyects.length;
      this.checkGoalInactive();
    });
  }

  checkGoalActive(): void {
    for (let i = 0; i < this.activeProyects!.length; i++) {
      if (this.activeProyects![i].goalAmount! > this.activeProyects![i].collected!) {
        this.totalComplete = this.totalComplete + 1;
      } else {
        this.totalIncomplete = this.totalIncomplete + 1;
      }
    }
  }

  checkGoalInactive(): void {
    for (let i = 0; i < this.inactiveProyects!.length; i++) {
      if (this.inactiveProyects![i].goalAmount! > this.inactiveProyects![i].collected!) {
        this.totalComplete = this.totalComplete + 1;
      } else {
        this.totalIncomplete = this.totalIncomplete + 1;
      }
    }
  }

  isAdmin(): boolean {
    return this.accountService.hasAnyAuthority(Authority.ADMIN);
  }

  loadMostFollowersProject(): void {
    this.favoriteService.getTop5Favorites().subscribe(data => {
      this.dataFollowersReport = data.body;
      const dataResult: any[] = [];
      for (let i = 0; i < this.dataFollowersReport.length; i++) {
        this.proyectService.find(this.dataFollowersReport[i].proyect_id).subscribe(data2 => {
          if (data2.body) this.labelsFollowers.push(data2.body.name);
        });
        dataResult.push(this.dataFollowersReport[i].count);
      }

      this.dataFollowers.push({
        label: 'Proyect Report',
        pointRadius: 0,
        pointHoverRadius: 0,
        backgroundColor: ['#fcc468', '#4acccd', '#fc68a0', '#68a0fc', '#c468fc'],
        borderWidth: 0,
        data: dataResult,
      });
    });
  }
}
