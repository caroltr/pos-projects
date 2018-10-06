import { DweetSettingsEnum } from './../../enum/DweetSettingsEnum';
import { DweetServiceProvider } from './../../providers/dweet-service/dweet-service';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';
import { Dweet } from '../../models/dweet';

@IonicPage()
@Component({
  selector: 'page-temperatura',
  templateUrl: 'temperatura.html',
})
export class TemperaturaPage implements OnInit, OnDestroy {

  private thingName: any;
  private dweet: Dweet;
  private isLoading: boolean = true;
  private time: any;

  private dataPlot: Array<any>;

  chart: Object;
  options: Object;

  saveInstance(chartInstance) {
    this.chart = chartInstance;
  }

  constructor(public navCtrl: NavController, public navParams: NavParams, public dweetService: DweetServiceProvider) {
    this.time = setInterval(() => {this.getLastDweets()}, 3000)
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad TemperaturaPage');
  }

  getLastDweets() {
    this.dataPlot = [];

    this.dweetService.loadLastDweets(this.thingName).subscribe(
      data => {
        
        this.preencherDweet(data)
      
      },
      err => console.log(),
      () => this.isLoading = false
    );
  }

  private preencherDweet(data: any) {
    this.dweet = this.dweetService.preencherDweet(data);
    this.loadDataForPlot(this.dweet)
    this.plotChart();
  }

  private loadDataForPlot(dweet:Dweet) {
    for (let _with of dweet.getWith()) {
      let epoch = new Date(_with.getCreated()).getTime();
      this.dataPlot.push([epoch, _with.getContent().getTemperatura()]);
    }
  }

  private plotChart() {
    this.options = {
      xAxis: {
        type: 'datetime'
      },
      yAxis: {
        labels: {
          formatter: function() {
            return this.value + 'oC';
          }
        }
      },
      title: { text: 'Temperatura' },
      series: [{
        name: 'temperatura',
        data: this.dataPlot.reverse(),
        pointInterval: 60*60
      }]
      };
  }

  ngOnInit() {
    this.thingName = DweetSettingsEnum.DWEET_THING_NAME;
    this.getLastDweets();
  }

  ngOnDestroy() {
    clearInterval(this.time);
  }
}
