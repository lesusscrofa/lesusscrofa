import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { WarningDialogComponent } from 'src/app/component/common/warning-dialog/warning-dialog.component';
import { DeliveryZoneEditionComponent } from 'src/app/component/delivery-zone/delivery-zone-edition/delivery-zone-edition.component';
import { DeliveryZoneListComponent } from 'src/app/component/delivery-zone/delivery-zone-list/delivery-zone-list.component';
import { DeliveryZone } from 'src/app/model/delivery-zone';
import { DeliveryZoneService } from 'src/app/service/delivery-zone.service';

@Component({
  selector: 'app-delivery-zones-edition',
  templateUrl: './delivery-zones-edition.component.html',
  styleUrls: ['./delivery-zones-edition.component.css']
})
export class DeliveryZonesEditionComponent implements OnInit {

  @ViewChild(DeliveryZoneListComponent) deliveryZoneList: DeliveryZoneListComponent;

  constructor(public dialog: MatDialog,
              private deliveryZoneService: DeliveryZoneService) { }

  ngOnInit(): void {
  }

  edit(deliveryMan: DeliveryZone): void {
    this.dialog.open(DeliveryZoneEditionComponent, {
      data: deliveryMan
    }).afterClosed().subscribe(fp => this.save(fp));
  }

  delete(deliveryZone: DeliveryZone): void {
    this.dialog.open(WarningDialogComponent, {
      data: "Etes-vous sûr de vouloir supprimer cette zone ?"
    }).afterClosed().subscribe(confirm => {
      if(confirm) {
        this.deliveryZoneService
          .delete(deliveryZone)
            .subscribe(fp => this.deliveryZoneList.search());
      }
    })
  }

  private save(deliveryZone: DeliveryZone): void {
    if(deliveryZone == null) {
      return;
    }

    if(deliveryZone.id) {
      this.deliveryZoneService
        .update(deliveryZone)
          .subscribe(fp => this.deliveryZoneList.search());
    }
    else {
      this.deliveryZoneService
        .create(deliveryZone)
          .subscribe(fp => this.deliveryZoneList.search());
    }
  }

}
