import { Component } from '@angular/core';
import { ViewController, NavParams } from 'ionic-angular';
import { StatusEnum } from '../../model/statusEnum';

/**
 * Generated class for the PopoverComponent component.
 *
 * See https://angular.io/api/core/Component for more info on Angular
 * Components.
 */
@Component({
  selector: 'popover',
  templateUrl: 'popover.html'
})
export class PopoverComponent {

  items = new Array
  statusFilter

  constructor(public viewCtrl: ViewController, public navParams:NavParams) {

    this.statusFilter = this.navParams.get('statusFilter');
    for (var enumMember in StatusEnum) {
      // Check if member is the value or the position
      var isValueProperty = parseInt(enumMember, 10) >= 0
      if (isValueProperty) {
        var member = StatusEnum[enumMember]
        this.items.push(member);

        console.log("statusFilter: " + this.statusFilter)
        console.log("member: " + member)
        console.log(this.statusFilter == member)
      }
   }

  }

  itemClick(item) {
    this.viewCtrl.dismiss(item);
  }

}
