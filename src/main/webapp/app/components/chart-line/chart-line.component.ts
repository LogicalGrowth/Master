import { Component, Input, OnInit } from '@angular/core';
import * as Chart from 'chart.js';
import { ChartDataSets } from 'chart.js';

@Component({
  selector: 'jhi-chart-line',
  templateUrl: './chart-line.component.html',
  styleUrls: ['../../../content/scss/paper-dashboard.scss'],
})
export class ChartLineComponent implements OnInit {
  @Input() labels: any[] = [];
  @Input() datasets: ChartDataSets[] = [];
  @Input() title: any;
  @Input() id: any;
  @Input() onchange: any;
  gradientStroke: any;
  chartColor = '#FFFFFF';
  canvas: any;
  ctx: any;
  gradientFill: any;
  myChart: any;

  constructor() {}

  ngOnInit(): void {
    this.canvas = document.querySelector(`#${this.id} canvas`);
    this.ctx = this.canvas.getContext('2d');

    this.gradientStroke = this.ctx.createLinearGradient(500, 0, 100, 0);
    this.gradientStroke.addColorStop(0, '#80b6f4');
    this.gradientStroke.addColorStop(1, this.chartColor);

    this.gradientFill = this.ctx.createLinearGradient(0, 170, 0, 50);
    this.gradientFill.addColorStop(0, 'rgba(128, 182, 244, 0)');
    this.gradientFill.addColorStop(1, 'rgba(249, 99, 59, 0.40)');

    this.myChart = new Chart(this.ctx, {
      type: 'line',
      data: {
        labels: this.labels,
        datasets: this.datasets,
      },
      options: {
        legend: {
          display: false,
        },

        tooltips: {
          enabled: true,
        },

        scales: {
          yAxes: [
            {
              ticks: {
                fontColor: '#9f9f9f',
                beginAtZero: false,
                maxTicksLimit: 5,
              },
              gridLines: {
                drawBorder: false,
                zeroLineColor: 'transparent',
                color: 'rgba(255,255,255,0.05)',
              },
            },
          ],

          xAxes: [
            {
              gridLines: {
                drawBorder: false,
                color: 'rgba(255,255,255,0.1)',
                zeroLineColor: 'transparent',
                display: false,
              },
              ticks: {
                padding: 20,
                fontColor: '#9f9f9f',
              },
            },
          ],
        },
      },
    });

    if (typeof this.onchange === 'function') {
      this.onchange(this.myChart);
    }
  }
}
