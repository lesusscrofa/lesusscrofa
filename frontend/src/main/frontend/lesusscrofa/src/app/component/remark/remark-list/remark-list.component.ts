import { Component, Input, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { Observable } from 'rxjs';
import { DailyOrderRemarkView } from 'src/app/model-view/daily-order-remark-view';
import { Client } from 'src/app/model/client';
import { RemarkService } from 'src/app/service/remark.service';

@Component({
  selector: 'app-remark-list',
  templateUrl: './remark-list.component.html',
  styleUrls: ['./remark-list.component.css']
})
export class RemarkListComponent implements OnInit {

  displayedColumns: string[] = ['clientFirstName', 'clientLastName', 'clientMessage', 'dailyMessage', 'foodsOrdersResume'];

  dataSource: MatTableDataSource<DailyOrderRemarkView>

  constructor(private remarkService: RemarkService) {
    this.dataSource = new MatTableDataSource();
   }

  ngOnInit(): void {

  }

  @Input()
  set day(day: Date) {
    this.remarkService.getDailyOrderRemark(day)
      .subscribe(r => this.dataSource.data = r);
  }
}
