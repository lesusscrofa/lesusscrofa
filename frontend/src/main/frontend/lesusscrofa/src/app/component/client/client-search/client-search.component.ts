import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormControl } from '@angular/forms';
import { Observable } from 'rxjs';
import { flatMap, map, startWith } from 'rxjs/operators';
import { Client } from 'src/app/model/client';
import { ClientService } from 'src/app/service/client.service';

@Component({
  selector: 'client-search',
  templateUrl: './client-search.component.html',
  styleUrls: ['./client-search.component.css']
})
export class ClientSearchComponent implements OnInit {

  clients$: Observable<Client[]>;

  private _client: Client;

  searchControl: FormControl = new FormControl();

  @Output() clientSelected = new EventEmitter();

  constructor(private clientService: ClientService,
              private fb: FormBuilder) { 
    this.clients$ = this.searchControl.valueChanges
      .pipe(
        startWith(''),
        map(client => client && client instanceof Client ? this.getClientName(client) : client),
        flatMap(value => value ? this.filter(value) : this.clientService.getClientsByName('')));
  }

  ngOnInit(): void {
    
  }

  private filter(value: string): Observable<Client[]> {
    return this.clientService.getClientsByName(value);
  }

  getClientName(client: Client) {
    return client ? client.firstName + ' ' + client.lastName : '';
  }

  handleEmptyInput(event: any, key: string){
    if(event.target.value === '') {
      this.clientSelected.emit(null);
    }
  }

  get client(): Client {
    return this._client;
  }

  @Input() set client(client: Client) {
    this._client = client;
    this.searchControl.setValue(this._client);
    this.clientSelected.emit(this.searchControl.value);
  }

}
