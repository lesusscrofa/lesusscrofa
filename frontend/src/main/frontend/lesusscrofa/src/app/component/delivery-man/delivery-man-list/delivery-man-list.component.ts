import { SelectionModel } from '@angular/cdk/collections';
import { AfterViewInit, Component, EventEmitter, OnInit, Output, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTable } from '@angular/material/table';
import { DeliveryMan } from 'src/app/model/delivery-man';
import { DeliveryManService } from 'src/app/service/delivery-man.service';
import { DeliveryManListDataSource } from './delivery-man-list-datasource';

@Component({
  selector: 'app-delivery-man-list',
  templateUrl: './delivery-man-list.component.html',
  styleUrls: ['./delivery-man-list.component.css']
})
export class DeliveryManListComponent implements AfterViewInit, OnInit {
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatTable) table: MatTable<DeliveryMan>;

  dataSource: DeliveryManListDataSource;

  displayedColumns = ['select', 'id', 'firstName', 'lastName'];

  selection : SelectionModel<DeliveryMan>;

  @Output()
  edit: EventEmitter<DeliveryMan>;

  @Output()
  delete: EventEmitter<DeliveryMan>;

  constructor(private fb: FormBuilder,
              private deliveryManService: DeliveryManService) {
    this.dataSource = new DeliveryManListDataSource();

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
    this.deliveryManService.findAll()
      .subscribe(
        fps => this.dataSource.loadData(fps)
      );
  }

  editSelectedRow() {
    const fp = this.selection.selected[0];

    this.edit.emit(fp);
  }

  deleteSelectedRow() {
    const fp = this.selection.selected[0];

    this.delete.emit(fp);
  }
}
