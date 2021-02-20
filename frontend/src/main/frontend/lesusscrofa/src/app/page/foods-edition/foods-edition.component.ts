import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { WarningDialogComponent } from 'src/app/component/common/warning-dialog/warning-dialog.component';
import { FoodEditionComponent } from 'src/app/component/food/food-edition/food-edition.component';
import { FoodListComponent } from 'src/app/component/food/food-list/food-list.component';
import { Food } from 'src/app/model/food';
import { FoodService } from 'src/app/service/food.service';

@Component({
  selector: 'app-foods-edition',
  templateUrl: './foods-edition.component.html',
  styleUrls: ['./foods-edition.component.css']
})
export class FoodsEditionComponent implements OnInit {

  @ViewChild(FoodListComponent) foodList: FoodListComponent;

  constructor(public dialog: MatDialog,
              private FoodService: FoodService) { }

  ngOnInit(): void {
  }

  edit(Food: Food): void {
    this.dialog.open(FoodEditionComponent, {
      data: Food
    }).afterClosed().subscribe(fp => this.save(fp));
  }

  delete(Food: Food): void {
    this.dialog.open(WarningDialogComponent, {
      data: "Etes-vous sûr de vouloir supprimer cet aliment ?"
    }).afterClosed().subscribe(confirm => {
      if(confirm) {
        this.FoodService
          .delete(Food)
            .subscribe(fp => this.foodList.search());
      }
    })
  }

  private save(Food: Food): void {
    if(Food == null) {
      return;
    }

    if(Food.id) {
      this.FoodService
        .update(Food)
          .subscribe(fp => this.foodList.search());
    }
    else {
      this.FoodService
        .create(Food)
          .subscribe(fp => this.foodList.search());
    }
  }
}
