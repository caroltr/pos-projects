import { BrowserModule } from '@angular/platform-browser';
import { ErrorHandler, NgModule } from '@angular/core';
import { IonicApp, IonicErrorHandler, IonicModule } from 'ionic-angular';
import { SplashScreen } from '@ionic-native/splash-screen';
import { StatusBar } from '@ionic-native/status-bar';

import { MyApp } from './app.component';
import { HomePage } from '../pages/home/home';
import { PedidoService } from '../services/pedidoService'
import { PopoverComponent } from '../components/popover/popover';
import { DetalhesPedidoPage } from '../pages/detalhes-pedido/detalhes-pedido';

@NgModule({
  declarations: [
    MyApp,
    HomePage,
    PopoverComponent,
    DetalhesPedidoPage
  ],
  imports: [
    BrowserModule,
    IonicModule.forRoot(MyApp)
  ],
  bootstrap: [IonicApp],
  entryComponents: [
    MyApp,
    HomePage,
    PopoverComponent,
    DetalhesPedidoPage
  ],
  providers: [
    StatusBar,
    SplashScreen,
    PedidoService,
    {provide: ErrorHandler, useClass: IonicErrorHandler}
  ]
})
export class AppModule {}
