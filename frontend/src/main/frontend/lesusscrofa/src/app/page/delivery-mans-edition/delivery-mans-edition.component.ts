import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { WarningDialogComponent } from 'src/app/component/common/warning-dialog/warning-dialog.component';
import { DeliveryManEditionComponent } from 'src/app/component/delivery-man/delivery-man-edition/delivery-man-edition.component';
import { DeliveryManListComponent } from 'src/app/component/delivery-man/delivery-man-list/delivery-man-list.component';
import { DeliveryMan } from 'src/app/model/delivery-man';
import { DeliveryManService } from 'src/app/service/delivery-man.service';

@Component({
  selector: 'app-delivery-mans-edition',
  templateUrl: './delivery-mans-edition.component.html',
  styleUrls: ['./delivery-mans-edition.component.css']
})
export class DeliveryMansEditionComponent implements OnInit {

  @ViewChild(DeliveryManListComponent) deliveryManList: DeliveryManListComponent;

  constructor(public dialog: MatDialog,
              private deliveryManService: DeliveryManService) { }

  ngOnInit(): void {
  }

  edit(deliveryMan: DeliveryMan): void {
    this.dialog.open(DeliveryManEditionComponent, {
      data: deliveryMan
    }).afterClosed().subscribe(fp => this.save(fp));
  }

  delete(deliveryMan: DeliveryMan): void {
    this.dialog.open(WarningDialogComponent, {
      data: "Etes-vous sûr de vouloir supprimer ce livreur ?"
    }).afterClosed().subscribe(confirm => {
      if(confirm) {
        this.deliveryManService
          .delete(deliveryMan)
            .subscribe(fp => this.deliveryManList.search());
      }
    })
  }

  private save(deliveryMan: DeliveryMan): void {
    if(deliveryMan == null) {
      return;
    }

    if(deliveryMan.id) {
      this.deliveryManService
        .update(deliveryMan)
          .subscribe(fp => this.deliveryManList.search());
    }
    else {
      this.deliveryManService
        .create(deliveryMan)
          .subscribe(fp => this.deliveryManList.search());
    }
  }
}
