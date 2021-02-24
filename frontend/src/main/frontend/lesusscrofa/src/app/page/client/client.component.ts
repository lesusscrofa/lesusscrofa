import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { EMPTY, Observable } from 'rxjs';
import { ClientCreationComponent } from 'src/app/component/client/client-creation/client-creation.component';
import { EncodeRemarkComponent } from 'src/app/component/remark/encode-remark/encode-remark.component';
import { Client } from 'src/app/model/client';
import { Remark } from 'src/app/model/remark';
import { ClientService } from 'src/app/service/client.service';
import { RemarkService } from 'src/app/service/remark.service';

@Component({
  selector: 'app-client',
  templateUrl: './client.component.html',
  styleUrls: ['./client.component.css']
})
export class ClientComponent implements OnInit {

  private _client: Client;

  remark: Observable<Remark>;

  constructor(private clientService: ClientService,
              private remarkService: RemarkService,
              public dialog: MatDialog) { 
  }

  ngOnInit(): void {
    
  }

  selectClient(client: Client) {
    this.client = client;
  }

  create(): void {
    this.dialog.open(ClientCreationComponent).afterClosed()
      .subscribe(client => client ? this.save(client): null);
  }

  save(client: Client) {
    if(client.id) {
      this.clientService
        .update(client)
        .subscribe(c => {this.client = c});
    }
    else {
      this.clientService.
        create(client)
        .subscribe(c => {this.client = c});
    }
  }

  editRemark(remark: Remark) {
    this.dialog.open(EncodeRemarkComponent, {data: remark})
      .afterClosed()
      .subscribe(r => this.saveRemark(remark));    
  }

  private saveRemark(remark: Remark) {
    if(remark.id != null && remark.message == '') {
      this.remark = this.remarkService.delete(remark);
    }
    else if(remark.message !== '') {
      this.remark = this.remarkService.save(remark);
    }
  }

  public get client(): Client {
    return this._client;
  }

  public set client(client: Client) {
    this._client = client;;

    if(client) {
      this.remark = this.remarkService.getClientRemark(this.client.id);
    }
    else {
      this.remark = EMPTY;
    }
  }
}
