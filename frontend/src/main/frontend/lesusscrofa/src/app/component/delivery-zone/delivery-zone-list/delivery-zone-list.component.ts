import { SelectionChange, SelectionModel } from '@angular/cdk/collections';
import { AfterViewInit, Component, EventEmitter, OnInit, Output, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTable } from '@angular/material/table';
import { DeliveryZone } from 'src/app/model/delivery-zone';
import { DeliveryZoneService } from 'src/app/service/delivery-zone.service';
import { DeliveryZoneListDataSource } from './delivery-zone-list-datasource';

@Component({
  selector: 'app-delivery-zone-list',
  templateUrl: './delivery-zone-list.component.html',
  styleUrls: ['./delivery-zone-list.component.css']
})
export class DeliveryZoneListComponent implements AfterViewInit, OnInit {
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatTable) table: MatTable<DeliveryZone>;

  dataSource: DeliveryZoneListDataSource;

  displayedColumns = ['select', 'id', 'name', 'deliveryMan'];

  selection : SelectionModel<DeliveryZone>;

  searchZoneControl: FormGroup;

  @Output()
  edit: EventEmitter<DeliveryZone>;

  @Output()
  delete: EventEmitter<DeliveryZone>;

  constructor(private fb: FormBuilder,
              private deliveryZoneService: DeliveryZoneService) {
    this.dataSource = new DeliveryZoneListDataSource();

    this.selection = new SelectionModel(false, []);

    this.edit = new EventEmitter();
    this.delete = new EventEmitter();

    this.searchZoneControl = this.fb.group({
      
    });

    this.searchZoneControl.valueChanges.subscribe(() => this.search());
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
    this.deliveryZoneService.findAll()
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
