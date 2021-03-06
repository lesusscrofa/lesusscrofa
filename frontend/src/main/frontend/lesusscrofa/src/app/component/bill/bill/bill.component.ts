import { Component, Input, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { MatDatepicker } from '@angular/material/datepicker';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { Client } from 'src/app/model/client';
import { BillService } from 'src/app/service/bill.service';
import { StringUtils } from 'src/app/utils/string-utils';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-bill',
  templateUrl: './bill.component.html',
  styleUrls: ['./bill.component.css']
})
export class BillComponent implements OnInit {

  month: FormControl;

  billUrl: SafeResourceUrl;

  private _client: Client;

  constructor(private billService: BillService,
              private sanitizer: DomSanitizer) {
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
    const month = monthDate.getMonth() + 1;
    const year = monthDate.getFullYear();

    this.billService.get(this.client.id, month, year).subscribe(pdf => {
      this.billUrl = this.sanitizer.bypassSecurityTrustResourceUrl(URL.createObjectURL(pdf));
    });

    console.log(this.billUrl);
  }

  @Input() 
  set client(client: Client) {
    this._client = client;
  }

  get client(): Client {
    return this._client;
  }
}
