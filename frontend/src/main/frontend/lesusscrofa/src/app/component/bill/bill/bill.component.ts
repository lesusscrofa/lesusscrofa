import { Component, Input, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { MatDatepicker } from '@angular/material/datepicker';
import { Observable } from 'rxjs';
import { Bill } from 'src/app/model/bill';
import { Client } from 'src/app/model/client';
import { BillService } from 'src/app/service/bill.service';

@Component({
  selector: 'app-bill',
  templateUrl: './bill.component.html',
  styleUrls: ['./bill.component.css']
})
export class BillComponent implements OnInit {

  month: FormControl;

  private _client: Client;

  private _bill$: Observable<Bill>;

  constructor(private billService: BillService) {
    this.month = new FormControl(new Date());
   }

  ngOnInit(): void {
    this.loadBill();
  }

  monthSelected(dateInput: Date, monthPicker: MatDatepicker<Date>) {
    const monthDate = this.month.value as Date;

    monthDate.setMonth(dateInput.getMonth());
    monthDate.setFullYear(dateInput.getFullYear());

    this.month.setValue(monthDate);

    monthPicker.close();

    this.loadBill();
  }

  private loadBill() {
    const monthDate = this.month.value as Date;

    this.bill$ = this.billService.get(this.client.id, monthDate.getMonth() + 1, monthDate.getFullYear());
  }

  set bill$(bill$: Observable<Bill>) {
    this._bill$ = bill$;
  }

  get bill$(): Observable<Bill> {
    return this._bill$;
  }

  @Input() 
  set client(client: Client) {
    this._client = client;
  }

  get client(): Client {
    return this._client;
  }
}
