import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from 'src/environments/environment';

import { Client } from '../model/client';

@Injectable({
  providedIn: 'root'
})
export class ClientService {

  private url: string;

  constructor(private http: HttpClient) { 
    this.url = environment.apiUrl + "/clients"
  }

  get(id: number): Observable<Client> {
    return this.http.get<any>(this.url + "/" + id)
        .pipe(
            map(client => Client.fromJson(client))
        );
  }

  getClientsByName(name: String): Observable<Client[]> {    
    return this.http.get<any>(this.url + "?name=" + name)
      .pipe(
        map(clients => clients.map(client => Client.fromJson(client)))
        );
  }

  create(client: Client): Observable<Client> {
    return this.http.post<Client>(this.url, client.toJson())
      .pipe(
        map(c => Client.fromJson(c))
      );
  }

  update(client: Client): Observable<Client> {
    return this.http.put<Client>(this.url + "/" + client.id, client.toJson())
      .pipe(
        map(c => Client.fromJson(c))
      );
  }
}
