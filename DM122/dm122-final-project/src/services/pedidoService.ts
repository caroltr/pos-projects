import { StatusPedido } from './../model/statusPedido';
import { Injectable } from '@angular/core'
import * as firebase from 'firebase'

@Injectable()
export class PedidoService {
    pedidos:any

    constructor() {
        this.initializeFirebase()
    }

    addPedido(pedido: StatusPedido) {
        this.pedidos.push(pedido)
    }

    editPedido(pedido: StatusPedido) {
        this.pedidos = this.pedidos.filter(t => t.id!= pedido.getIdPedido())
        this.pedidos.push(this)
    }

    getFirebaseRef() {
        return firebase.database().ref('statuspedido')
    }

    initializeFirebase() {

        const config = {
            apiKey: "AIzaSyCXq9CoPsatKjyldlabSlANyYRuyr5joqA",
            authDomain: "dm122-projeto-final-c559c.firebaseapp.com",
            databaseURL: "https://dm122-projeto-final-c559c.firebaseio.com",
            projectId: "dm122-projeto-final-c559c",
            storageBucket: "dm122-projeto-final-c559c.appspot.com",
            messagingSenderId: "1082534309123"
        };

        firebase.initializeApp(config);
    }
}