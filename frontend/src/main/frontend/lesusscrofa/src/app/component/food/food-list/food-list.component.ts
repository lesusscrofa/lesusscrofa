import { SelectionModel } from '@angular/cdk/collections';
import { AfterViewInit, Component, EventEmitter, OnInit, Output, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTable } from '@angular/material/table';
import { Food } from 'src/app/model/food';
import { FoodService } from 'src/app/service/food.service';
import { FoodListDataSource } from './food-list-datasource';

@Component({
  selector: 'app-food-list',
  templateUrl: './food-list.component.html',
  styleUrls: ['./food-list.component.css']
})
export class FoodListComponent implements AfterViewInit, OnInit {
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatTable) table: MatTable<Food>;

  dataSource: FoodListDataSource;

  displayedColumns = ['select', 'id', 'name', 'start', 'end', 'price', 'vat', 'unit'];

  selection : SelectionModel<Food>;

  @Output()
  edit: EventEmitter<Food>;

  @Output()
  delete: EventEmitter<Food>;

  constructor(private fb: FormBuilder,
              private foodService: FoodService) {
    this.dataSource = new FoodListDataSource();

    this.selection = new SelectionModel(false, []);

    this.edit = new EventEmitter();
    this.delete = new EventEmitter();
  }

  ngOnInit() {
    this.search();
  }

  ngAfterViewInit() {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
    this.table.dataSource = this.dataSource;
  }

  search() {
    this.foodService.getAllOtherFoods()
      .subscribe(
        foods => this.dataSource.loadData(foods)
      );
  }

  editSelectedRow() {
    const food = this.selection.selected[0];

    this.edit.emit(food);
  }

  deleteSelectedRow() {
    const food = this.selection.selected[0];

    this.delete.emit(food);
  }
}
