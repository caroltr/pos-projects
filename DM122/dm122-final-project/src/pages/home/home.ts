import { DetalhesPedidoPage } from './../detalhes-pedido/detalhes-pedido';
import { PedidoService } from './../../services/pedidoService';
import { Component } from '@angular/core';
import { NavController, PopoverController } from 'ionic-angular';
import { PopoverComponent } from '../../components/popover/popover';

@Component({
  selector: 'page-home',
  templateUrl: 'home.html'
})
export class HomePage {

  allPedidos = new Array
  displayPedidos = new Array
  statusFilter;

  constructor(public navCtrl: NavController, private pedidoService: PedidoService,
    public popoverCtrl: PopoverController) {

    this.getPedidos()
  }

  getPedidos() {
    this.pedidoService.getFirebaseRef().orderByChild("dataAtualizacao").on(
      'value',(dataSnapshot) => {

        let values = []

        dataSnapshot.forEach(function(child) {
          // Here, the children are sorted in ascending order

          console.log("item: " + child.key)
          let item = child.val()
        
          console.log("item: " + item);

              values.push({
                  idPedido: child.key,
                  dataEmissao: new Date(item.dataEmissao).toLocaleString(),
                  dataAtualizacao: new Date(item.dataAtualizacao).toLocaleString(),
                  vendedor: item.vendedor,
                  frete: "R$ " + item.frete.toFixed(2).toString().replace(".",","),
                  transportadora: item.transportadora,
                  status: item.status
              })
        })

        this.allPedidos = values.reverse()
        this.displayPedidos = this.allPedidos
      })
    }

  itemClicked(item) {
    this.navCtrl.push(DetalhesPedidoPage, {item});
  }

  showFilterPopover(event) {

    let filterPopover = this.popoverCtrl.create(PopoverComponent, {statusFilter:this.statusFilter});
    filterPopover.present({
      ev: event
    });

    filterPopover.onDidDismiss(selectedStatus => {

      if(selectedStatus == null) {
        // DO nothing
      } else if(selectedStatus == 0) {
        this.displayPedidos = this.allPedidos
        this.statusFilter = null
      } else {
        this.statusFilter = selectedStatus
        // List pedidos with the selected status
        this.displayPedidos = this.allPedidos.filter(m => m.status == selectedStatus)
      }
    })
  }

}
