import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';
import { StatusEnum } from './../../model/statusEnum';
import { PedidoService } from '../../services/pedidoService';

@IonicPage()
@Component({
  selector: 'page-detalhes-pedido',
  templateUrl: 'detalhes-pedido.html',
})

export class DetalhesPedidoPage {

  item;
  keys = new Array;

  private newStatus;

  constructor(public navCtrl: NavController, public pedidoService: PedidoService,  public navParams: NavParams) {
    this.item = navParams.get("item");
    console.log("Item selecionado: " + this.item);

    for (var enumMember in StatusEnum) {
      // Check if member is the value or the position
      var isValueProperty = parseInt(enumMember, 10) >= 0
      if (isValueProperty) {
        var member = StatusEnum[enumMember]
        this.keys.push(member);
      }
   }

    console.log("Enum: " + this.keys);

  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad DetalhesPedidoPage');
  }

  save() {

    var timestampNow = new Date().getTime();
    console.log(timestampNow)
    
    if(this.newStatus == null) {
      this.newStatus = this.item.status
    }

    // Change the status
    this.pedidoService.getFirebaseRef().child(this.item.idPedido).update({
          status: this.newStatus,
          dataAtualizacao: timestampNow
    })

    this.navCtrl.pop();
  }


  selectedStatus(newStatus) {
    console.log("Status modificado para " + newStatus)
    this.newStatus = newStatus
  }
}
