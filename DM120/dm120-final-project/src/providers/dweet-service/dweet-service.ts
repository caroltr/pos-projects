import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import 'rxjs/add/operator/map';
import { DweetSettingsEnum } from '../../enum/DweetSettingsEnum';
import { With } from '../../models/with';
import { Dweet } from '../../models/dweet';
import { Content } from '../../models/content';

@Injectable()
export class DweetServiceProvider {

  private dweetioApiGetUrl = DweetSettingsEnum.DWEET_URL_GET_ALL
  private dweetioApiPostUrl = DweetSettingsEnum.DWEET_URL_POST

  constructor(public http: HttpClient) {
    console.log('Hello DweetServiceProvider Provider');
  }

  loadLastDweets(thingName: string) {
    var url = this.dweetioApiGetUrl + thingName
    
    console.log("GET URL: " + url)

    return this.http.get(url)
  }

  postBuzzerState(thingName: string, buzzerState: string) {

    var url = this.dweetioApiPostUrl + thingName

    console.log("POST URL: " + url)

    let header: any = {
      "Content-Type": "applicationJson"
    }

    let body = {
      buzzer: buzzerState
    }

    return this.http.post(url, body, header);
  }

  preencherDweet(data: any) {

    console.log(data)

    let dweet: Dweet;
    let _withs: Array<With>;
    let _date: string;
    let _time: string;

     _withs = new Array<With>();

    for (let _with of data.with) {

      var botao
      var estadoBotao = _with.content.botao
      if(estadoBotao == "1") {
        botao = "pressionado"
      } else if (estadoBotao == "0") {
        botao = "não pressionado"
      }

      var toque
      var estadoSensorToque= _with.content.toque
      if(estadoSensorToque == "1") {
        toque = "pressionado"
      } else if (estadoSensorToque == "0") {
        toque = "não pressionado"
      }

      let tempContent: Content;
      tempContent = new Content(_with.content.temperatura, _with.content.luminosidade,
        botao, toque, _with.content.status)

      _date = this.formatDate(_with.created);
      _time = this.formatTime(_with.created);

      let tempWith: With
      tempWith = new With(_with.thing, _with.created,tempContent, _date, _time)

      _withs.push(tempWith)
    }

    dweet = new Dweet(data.this, data.by, data.the, _withs)

    return dweet
  }

  private formatDate(date: any): string {

    let originalDate: string = date
    var dateParse = originalDate.slice(0, 10)

    return dateParse
  }

  private formatTime(date: any): string {

    let originalDate: string = date
    var timeParse = originalDate.slice(11, 19)

    return timeParse
  }

}
