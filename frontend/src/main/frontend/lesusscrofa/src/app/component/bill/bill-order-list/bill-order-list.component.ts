import { Component, Input, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { OrderSummary } from 'src/app/model/order-summary';
import { Order } from 'src/app/model/order';

@Component({
  selector: 'app-bill-order-list',
  templateUrl: './bill-order-list.component.html',
  styleUrls: ['./bill-order-list.component.css']
})
export class BillOrderListComponent implements OnInit {

  private _billOrdersSummaries: OrderSummary[];

  displayedColumns: string[] = ['description', 'quantity', 'unit', 'unitPriceReductionIncluded', 'unitPriceVatIncluded', 'vat', 'totalVat', 'totalVatIncluded'];

  dataSource: MatTableDataSource<OrderSummary>;

  constructor() { 
    this.dataSource = new MatTableDataSource();
  }

  ngOnInit(): void {
  }

  get billOrdersSummaries(): OrderSummary[] {
    return this._billOrdersSummaries;
  }

  @Input()
  set billOrdersSummaries(billOrdersSummaries: OrderSummary[]) {
    this._billOrdersSummaries = billOrdersSummaries;
    this.dataSource.data = this._billOrdersSummaries;
  }

}
