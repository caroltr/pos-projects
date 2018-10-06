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
            apiKey: "API_KEY",
            authDomain: "AUTH_DOMAIN",
            databaseURL: "URL",
            projectId: "PROJECT_ID",
            storageBucket: "BUCKET",
            messagingSenderId: "SENDER_ID"
        };

        firebase.initializeApp(config);
    }
}