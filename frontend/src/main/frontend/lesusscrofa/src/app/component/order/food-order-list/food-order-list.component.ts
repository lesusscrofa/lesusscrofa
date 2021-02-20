import { Component, Input, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { FoodOrderView } from 'src/app/model-view/food-order-view';

@Component({
  selector: 'app-food-order-list',
  templateUrl: './food-order-list.component.html',
  styleUrls: ['./food-order-list.component.css']
})
export class FoodOrderListComponent implements OnInit {

  private _foodsOrders: FoodOrderView[];

  displayedColumns: string[] = ['foodName', 'foodService', 'quantity'];

  dataSource: MatTableDataSource<FoodOrderView>;

  constructor() { 
    this.dataSource = new MatTableDataSource();
  }

  ngOnInit(): void {
  }

  get foodsOrders(): FoodOrderView[] {
    return this._foodsOrders;
  }

  @Input()
  set foodsOrders(foodsOrders: FoodOrderView[]) {
    this._foodsOrders = foodsOrders;
    this.dataSource.data = this._foodsOrders;
  }

}
