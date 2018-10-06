import { TemperaturaPage } from './../temperatura/temperatura';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { NavController } from 'ionic-angular';
import { LuminiosidadePage } from '../luminiosidade/luminiosidade';
import { DweetSettingsEnum } from './../../enum/DweetSettingsEnum';
import { DweetServiceProvider } from './../../providers/dweet-service/dweet-service';
import { Dweet } from '../../models/dweet';

@Component({
  selector: 'page-home',
  templateUrl: 'home.html'
})
export class Home implements OnInit, OnDestroy {

  private isLoading: boolean = true;
  private thingName: any;
  private thingNameBuzzer: any;
  private dweet: Dweet;
  private time: any;

  constructor(public navCtrl: NavController, public dweetService: DweetServiceProvider) {
    this.time = setInterval(() => {this.getLastDweets()}, 3000)
  }

  ngOnInit() {
    this.thingName = DweetSettingsEnum.DWEET_THING_NAME;
    this.thingNameBuzzer = DweetSettingsEnum.DWEET_THING_BUZZER_NAME;
    this.getLastDweets();
  }

  ngOnDestroy() {
    clearInterval(this.time);
}

  goToTempPage() {
    this.navCtrl.push(TemperaturaPage)
  }

  goToLumPage() {
    this.navCtrl.push(LuminiosidadePage)
  }

  notify(event) {
    var activateBuzzer = event.checked ? "1" : "0"

    this.dweetService.postBuzzerState(this.thingNameBuzzer, activateBuzzer).subscribe(
      data => {
        
        console.log("sucesso ao publicar estado do buzzer!");
      
      },
      err => console.log("falha ao publicar estado do buzzer!")
    );
  }

  getLastDweets() {

    this.dweetService.loadLastDweets(this.thingName).subscribe(
      data => this.preencherDweet(data),
      err => console.log(err),
      () => this.isLoading = false
    );
  }

  private preencherDweet(data: any) {
    this.dweet = this.dweetService.preencherDweet(data);
  }
}