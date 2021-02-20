import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Observable } from 'rxjs';
import { WarningDialogComponent } from 'src/app/component/common/warning-dialog/warning-dialog.component';
import { FormulaPriceEditionComponent } from 'src/app/component/formula-price/formula-price-edition/formula-price-edition.component';
import { FormulaPriceListComponent } from 'src/app/component/formula-price/formula-price-list/formula-price-list.component';
import { FormulaPrice } from 'src/app/model/formula-price';
import { FormulaPriceService } from 'src/app/service/formula-price.service';

@Component({
  selector: 'app-formula-prices-edition',
  templateUrl: './formula-prices-edition.component.html',
  styleUrls: ['./formula-prices-edition.component.css']
})
export class FormulaPricesEditionComponent implements OnInit {

  @ViewChild(FormulaPriceListComponent) formulaPriceList: FormulaPriceListComponent;

  constructor(public dialog: MatDialog,
    private formulaPriceService: FormulaPriceService) { 

  }

  ngOnInit(): void {
    
  }

  edit(price: FormulaPrice): void {
    this.dialog.open(FormulaPriceEditionComponent, {
      data: price
    }).afterClosed().subscribe(fp => this.savePrice(fp));
  }

  delete(price: FormulaPrice): void {
    this.dialog.open(WarningDialogComponent, {
      data: "Etes-vous sûr de vouloir supprimer ce prix ?"
    }).afterClosed().subscribe(confirm => {
      if(confirm) {
        this.formulaPriceService
          .delete(price)
            .subscribe(fp => this.formulaPriceList.search());
      }
    })
  }

  savePrice(price: FormulaPrice): void {
    if(price == null) {
      return;
    }

    if(price.id) {
      this.formulaPriceService
        .update(price)
          .subscribe(fp => this.formulaPriceList.search());
    }
    else {
      this.formulaPriceService
        .create(price)
          .subscribe(fp => this.formulaPriceList.search());
    }
  }
}
