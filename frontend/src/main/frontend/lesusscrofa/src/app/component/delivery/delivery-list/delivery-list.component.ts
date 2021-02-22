import {AfterViewInit, Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {MatTable} from '@angular/material/table';
import { DeliveryView } from 'src/app/model-view/delivery-view';
import {CdkDragDrop} from "@angular/cdk/drag-drop";
import {DeliveryListDatasource} from "./delivery-list-datasource";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";

@Component({
  selector: 'app-delivery-list',
  templateUrl: './delivery-list.component.html',
  styleUrls: ['./delivery-list.component.css']
})
export class DeliveryListComponent implements OnInit, AfterViewInit {
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatTable) table: MatTable<DeliveryView>;

  private _deliveries: DeliveryView[];

  displayedColumns: string[] = ['deliveryPosition', 'firstName', 'lastName', "soupQuantity", "dishQuantity", "alternativeDishQuantity", "dessertQuantity", "others"];

  dataSource: DeliveryListDatasource;

  @Output()
  private updateClientDeliveryPosition: EventEmitter<{clientIdToUpdate: number, newDeliveryPosition : number}>;

  constructor() { 
    this.dataSource = new DeliveryListDatasource();
    this.updateClientDeliveryPosition = new EventEmitter<{clientIdToUpdate: number; newDeliveryPosition: number}>();
  }

  ngOnInit(): void {

  }

  ngAfterViewInit() {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
    this.table.dataSource = this.dataSource;
  }

  get foodsOrders(): DeliveryView[] {
    return this._deliveries;
  }

  onListDrop(event: CdkDragDrop<DeliveryView[]>) {
    const clientIdToUpdate = this._deliveries[event.previousIndex].id;
    const newDeliveryPosition = event.currentIndex+1;

    this.updateClientDeliveryPosition.emit({clientIdToUpdate, newDeliveryPosition});
  }

  @Input()
  set deliveries(deliveries: DeliveryView[]) {
    this._deliveries = deliveries;
    this.dataSource.loadData(deliveries);
  }

}
