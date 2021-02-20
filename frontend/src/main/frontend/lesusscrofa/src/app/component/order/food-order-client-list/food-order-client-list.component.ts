import {AfterViewInit, Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {MatTable} from '@angular/material/table';
import { FoodOrderClientFlatView } from 'src/app/model-view/food-order-client-flat-view';
import {CdkDragDrop} from "@angular/cdk/drag-drop";
import {FoodOrderClientListDatasource} from "./food-order-client-list-datasource";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";

@Component({
  selector: 'app-food-order-client-list',
  templateUrl: './food-order-client-list.component.html',
  styleUrls: ['./food-order-client-list.component.css']
})
export class FoodOrderClientListComponent implements OnInit, AfterViewInit {
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatTable) table: MatTable<FoodOrderClientFlatView>;

  private _foodsOrdersClients: FoodOrderClientFlatView[];

  displayedColumns: string[] = ['deliveryPosition', 'firstName', 'lastName', "soupQuantity", "dishQuantity", "alternativeDishQuantity", "dessertQuantity", "others"];

  dataSource: FoodOrderClientListDatasource;

  @Output()
  private updateClientDeliveryPosition: EventEmitter<{clientIdToUpdate: number, newDeliveryPosition : number}>;

  constructor() { 
    this.dataSource = new FoodOrderClientListDatasource();
    this.updateClientDeliveryPosition = new EventEmitter<{clientIdToUpdate: number; newDeliveryPosition: number}>();
  }

  ngOnInit(): void {

  }

  ngAfterViewInit() {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
    this.table.dataSource = this.dataSource;
  }

  get foodsOrders(): FoodOrderClientFlatView[] {
    return this._foodsOrdersClients;
  }

  onListDrop(event: CdkDragDrop<FoodOrderClientFlatView[]>) {
    const clientIdToUpdate = this._foodsOrdersClients[event.previousIndex].id;
    const newDeliveryPosition = event.currentIndex+1;

    this.updateClientDeliveryPosition.emit({clientIdToUpdate, newDeliveryPosition});
  }

  @Input()
  set foodsOrdersClients(foodsOrdersClients: FoodOrderClientFlatView[]) {
    this._foodsOrdersClients = foodsOrdersClients;
    this.dataSource.loadData(foodsOrdersClients);
  }

}
