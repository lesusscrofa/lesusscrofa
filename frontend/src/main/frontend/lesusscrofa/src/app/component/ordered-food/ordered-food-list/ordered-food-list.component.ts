import { Component, Input, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { OrderedFoodView } from 'src/app/model-view/ordered-food-view';

@Component({
  selector: 'app-ordered-food-list',
  templateUrl: './ordered-food-list.component.html',
  styleUrls: ['./ordered-food-list.component.css']
})
export class OrderedFoodListComponent implements OnInit {

  private _orderedFoods: OrderedFoodView[];

  displayedColumns: string[] = ['foodName', 'foodService', 'quantity'];

  dataSource: MatTableDataSource<OrderedFoodView>;

  constructor() { 
    this.dataSource = new MatTableDataSource();
  }

  ngOnInit(): void {
  }

  get orderedFoods(): OrderedFoodView[] {
    return this._orderedFoods;
  }

  @Input()
  set orderedFoods(orderedFoods: OrderedFoodView[]) {
    this._orderedFoods = orderedFoods;
    this.dataSource.data = this._orderedFoods;
  }

}
