import {AfterViewInit, Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {MatTable, MatTableDataSource} from '@angular/material/table';
import { DeliveryView } from 'src/app/model-view/delivery-view';
import {CdkDragDrop, moveItemInArray} from "@angular/cdk/drag-drop";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import { ClientService } from 'src/app/service/client.service';

@Component({
  selector: 'app-delivery-list',
  templateUrl: './delivery-list.component.html',
  styleUrls: ['./delivery-list.component.css']
})
export class DeliveryListComponent implements OnInit, AfterViewInit {
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatTable) table: MatTable<DeliveryView>;

  @Input()
  isLoading: boolean;

  displayedColumns: string[] = ['deliveryPosition', 'firstName', 'lastName', "soupQuantity", "dishQuantity", "alternativeDishQuantity", "dessertQuantity", "others"];

  dataSource: MatTableDataSource<DeliveryView>;

  @Output()
  private updateClientDeliveryPosition: EventEmitter<{clientIdToUpdate: number, newDeliveryPosition : number}>;

  constructor(private clientService: ClientService) { 
    this.dataSource = new MatTableDataSource();
    this.updateClientDeliveryPosition = new EventEmitter<{clientIdToUpdate: number; newDeliveryPosition: number}>();
  }

  ngOnInit(): void {

  }

  ngAfterViewInit() {
    
  }

  onListDrop(event: CdkDragDrop<DeliveryView[]>) {
    const clientIdToUpdate = this.dataSource.data[event.previousIndex].id;
    const newDeliveryPosition = this.dataSource.data[event.currentIndex].deliveryPosition;

    moveItemInArray(this.dataSource.data, event.previousIndex, event.currentIndex);
    this.table.renderRows();

    this.updateClientDeliveryPosition.emit({clientIdToUpdate, newDeliveryPosition});
  }

  @Input()
  set deliveries(deliveries: DeliveryView[]) {
    this.dataSource.data = deliveries;
  }

  
}
